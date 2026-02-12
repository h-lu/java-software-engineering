-- H2 兼容的测试数据库 schema
-- 注意：H2 默认将未引用的标识符转换为大写

-- 图书表
CREATE TABLE IF NOT EXISTS books (
    isbn VARCHAR(50) PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    author VARCHAR(200) NOT NULL,
    available BOOLEAN DEFAULT TRUE
);

-- 索引优化查询
CREATE INDEX IF NOT EXISTS idx_books_title ON books(title);
CREATE INDEX IF NOT EXISTS idx_books_author ON books(author);
