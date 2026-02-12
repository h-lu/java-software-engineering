/**
 * 示例：健壮的 API 客户端
 * 运行方式：在浏览器控制台中运行，或引入到 HTML 文件中使用
 * 预期输出：能够正确处理各种 API 调用场景，包括错误和超时
 *
 * 本例演示：
 * 1. async/await 模式
 * 2. 错误处理（HTTP 错误、网络错误）
 * 3. 超时处理
 * 4. 返回值处理
 * 5. 重试机制
 */

// ============================================
// 配置
// ============================================
const API_CONFIG = {
    // API 基础 URL
    BASE_URL: 'http://localhost:7070',
    // 默认超时时间（毫秒）
    TIMEOUT: 10000,
    // 最大重试次数
    MAX_RETRIES: 3,
    // 重试延迟（毫秒）
    RETRY_DELAY: 1000
};

// ============================================
// 核心 API 客户端类
// ============================================

class ApiClient {
    constructor(config = {}) {
        this.baseUrl = config.baseUrl || API_CONFIG.BASE_URL;
        this.timeout = config.timeout || API_CONFIG.TIMEOUT;
        this.maxRetries = config.maxRetries || API_CONFIG.MAX_RETRIES;
        this.retryDelay = config.retryDelay || API_CONFIG.RETRY_DELAY;
    }

    /**
     * 发送 HTTP 请求（带超时和重试）
     *
     * @param {string} endpoint - API 端点（如 '/tasks'）
     * @param {Object} options - fetch 选项
     * @returns {Promise<Object>} 解析后的 JSON 数据
     * @throws {ApiError} 请求失败时抛出
     */
    async request(endpoint, options = {}) {
        const url = `${this.baseUrl}${endpoint}`;
        let lastError;

        // 重试循环
        for (let attempt = 0; attempt < this.maxRetries; attempt++) {
            try {
                return await this._doRequest(url, options);
            } catch (error) {
                lastError = error;

                // 如果是客户端错误（4xx），不重试
                if (error.status >= 400 && error.status < 500) {
                    throw error;
                }

                // 最后一次尝试失败，抛出错误
                if (attempt === this.maxRetries - 1) {
                    throw error;
                }

                // 等待后重试
                console.warn(`请求失败，${this.retryDelay}ms 后重试...`, error.message);
                await this._delay(this.retryDelay);
            }
        }

        throw lastError;
    }

    /**
     * 执行单次请求（带超时）
     */
    async _doRequest(url, options) {
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), this.timeout);

        try {
            const response = await fetch(url, {
                ...options,
                headers: {
                    'Content-Type': 'application/json',
                    ...options.headers
                },
                signal: controller.signal
            });

            clearTimeout(timeoutId);

            // 处理 HTTP 错误状态
            if (!response.ok) {
                throw new ApiError(
                    `HTTP ${response.status}: ${response.statusText}`,
                    response.status,
                    await this._parseErrorBody(response)
                );
            }

            // 处理空响应（如 204 No Content）
            if (response.status === 204) {
                return null;
            }

            return await response.json();
        } catch (error) {
            clearTimeout(timeoutId);

            // 处理超时
            if (error.name === 'AbortError') {
                throw new ApiError(
                    '请求超时，请检查网络连接',
                    0,
                    { type: 'TIMEOUT' }
                );
            }

            // 处理网络错误
            if (error.message.includes('fetch') || error.message.includes('network')) {
                throw new ApiError(
                    '网络错误，请检查网络连接',
                    0,
                    { type: 'NETWORK_ERROR', original: error.message }
                );
            }

            throw error;
        }
    }

    /**
     * 解析错误响应体
     */
    async _parseErrorBody(response) {
        try {
            return await response.json();
        } catch {
            return { message: response.statusText };
        }
    }

    /**
     * 延迟函数
     */
    _delay(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    // ============================================
    // 便捷方法
    // ============================================

    async get(endpoint) {
        return this.request(endpoint, { method: 'GET' });
    }

    async post(endpoint, data) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }

    async put(endpoint, data) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }

    async patch(endpoint, data) {
        return this.request(endpoint, {
            method: 'PATCH',
            body: JSON.stringify(data)
        });
    }

    async delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    }
}

/**
 * 自定义 API 错误类
 */
class ApiError extends Error {
    constructor(message, status = 0, data = {}) {
        super(message);
        this.name = 'ApiError';
        this.status = status;
        this.data = data;
        this.isClientError = status >= 400 && status < 500;
        this.isServerError = status >= 500;
    }
}

// ============================================
// CampusFlow 专用 API 客户端
// ============================================

class CampusFlowApiClient extends ApiClient {
    constructor(config = {}) {
        super(config);
    }

    // 任务管理 API
    async getAllTasks() {
        return this.get('/tasks');
    }

    async getTask(id) {
        return this.get(`/tasks/${id}`);
    }

    async createTask(taskData) {
        // 客户端验证
        this._validateTaskData(taskData);
        return this.post('/tasks', taskData);
    }

    async updateTask(id, taskData) {
        return this.put(`/tasks/${id}`, taskData);
    }

    async patchTask(id, partialData) {
        return this.patch(`/tasks/${id}`, partialData);
    }

    async deleteTask(id) {
        return this.delete(`/tasks/${id}`);
    }

    async completeTask(id) {
        return this.post(`/tasks/${id}/complete`);
    }

    async getOverdueFee(id) {
        return this.get(`/tasks/${id}/overdue-fee`);
    }

    // 统计 API
    async getStats() {
        return this.get('/stats');
    }

    // 健康检查
    async healthCheck() {
        return this.get('/health');
    }

    /**
     * 验证任务数据
     */
    _validateTaskData(data) {
        if (!data.title || data.title.trim() === '') {
            throw new ApiError('任务标题不能为空', 400, { type: 'VALIDATION_ERROR' });
        }

        if (!data.dueDate) {
            throw new ApiError('截止日期不能为空', 400, { type: 'VALIDATION_ERROR' });
        }

        // 验证日期格式
        const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
        if (!dateRegex.test(data.dueDate)) {
            throw new ApiError('日期格式不正确，应为 YYYY-MM-DD', 400, { type: 'VALIDATION_ERROR' });
        }
    }
}

// ============================================
// 使用示例
// ============================================

// 创建客户端实例
const api = new CampusFlowApiClient({
    baseUrl: 'http://localhost:7070',
    timeout: 10000
});

// 示例 1：获取所有任务
async function example1_getAllTasks() {
    try {
        console.log('正在获取任务列表...');
        const result = await api.getAllTasks();
        console.log('任务列表:', result);
        return result;
    } catch (error) {
        console.error('获取失败:', error.message);
        throw error;
    }
}

// 示例 2：创建任务（带验证）
async function example2_createTask() {
    try {
        const newTask = {
            title: '学习 API 客户端',
            description: '掌握健壮的 API 调用方法',
            dueDate: '2026-02-20'
        };

        console.log('正在创建任务...');
        const created = await api.createTask(newTask);
        console.log('创建成功:', created);
        return created;
    } catch (error) {
        if (error instanceof ApiError && error.isClientError) {
            console.error('请求参数错误:', error.message);
        } else {
            console.error('创建失败:', error.message);
        }
        throw error;
    }
}

// 示例 3：错误处理（网络错误、超时、HTTP 错误）
async function example3_errorHandling() {
    try {
        // 尝试获取不存在的任务
        const task = await api.getTask('99999');
        console.log('任务:', task);
    } catch (error) {
        if (error instanceof ApiError) {
            switch (error.status) {
                case 404:
                    console.error('任务不存在');
                    break;
                case 400:
                    console.error('请求参数错误:', error.data.message);
                    break;
                case 500:
                    console.error('服务器内部错误');
                    break;
                case 0:
                    if (error.data.type === 'TIMEOUT') {
                        console.error('请求超时，请检查网络');
                    } else if (error.data.type === 'NETWORK_ERROR') {
                        console.error('网络错误，请检查网络连接');
                    }
                    break;
                default:
                    console.error('未知错误:', error.message);
            }
        } else {
            console.error('意外错误:', error);
        }
    }
}

// 示例 4：批量操作
async function example4_batchOperations() {
    const operations = [
        api.getAllTasks(),
        api.getStats(),
        api.healthCheck()
    ];

    try {
        // 并行执行多个请求
        const [tasks, stats, health] = await Promise.all(operations);
        console.log('任务:', tasks);
        console.log('统计:', stats);
        console.log('健康:', health);
    } catch (error) {
        console.error('批量操作失败:', error.message);
    }
}

// 示例 5：带超时的请求
async function example5_timeout() {
    const fastApi = new CampusFlowApiClient({
        baseUrl: 'http://localhost:7070',
        timeout: 1000  // 1 秒超时（用于测试）
    });

    try {
        const result = await fastApi.getAllTasks();
        console.log('结果:', result);
    } catch (error) {
        if (error.data?.type === 'TIMEOUT') {
            console.error('请求超时了！');
        }
    }
}

// ============================================
// 导出（如果在模块环境中）
// ============================================

if (typeof module !== 'undefined' && module.exports) {
    module.exports = { ApiClient, CampusFlowApiClient, ApiError };
}

/*
 * 使用说明：
 *
 * 1. 直接在浏览器中使用：
 *    <script src="10_api_client.js"></script>
 *    <script>
 *        const api = new CampusFlowApiClient();
 *        api.getAllTasks().then(console.log);
 *    </script>
 *
 * 2. 作为模块导入：
 *    import { CampusFlowApiClient } from './10_api_client.js';
 *    const api = new CampusFlowApiClient();
 *
 * 3. 在 HTML 中内联使用：
 *    复制 ApiClient 和 CampusFlowApiClient 类到你的 app.js 中
 *
 * 关键特性：
 * - 自动处理 JSON 序列化/反序列化
 * - 统一的错误处理（区分客户端错误、服务器错误、网络错误）
 * - 请求超时保护
 * - 自动重试（仅对服务器错误和网络错误）
 * - 客户端数据验证
 */
