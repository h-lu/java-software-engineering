package com.campusflow.repository;

import com.campusflow.model.Book;
import java.util.List;
import java.util.Optional;

/**
 * 图书仓储接口
 * 定义对图书数据的 CRUD 操作
 */
public interface BookRepository {

    /**
     * 保存图书（新增或更新）
     *
     * @param book 图书对象
     * @throws IllegalArgumentException 如果 book 为 null 或 ISBN 为空
     * @throws RuntimeException 如果数据库操作失败
     */
    void save(Book book);

    /**
     * 根据 ISBN 查找图书
     *
     * @param isbn ISBN 编号
     * @return 包含图书的 Optional，如果不存在返回 empty
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * 查询所有图书
     *
     * @return 图书列表
     */
    List<Book> findAll();

    /**
     * 更新图书信息
     *
     * @param book 图书对象
     * @throws IllegalArgumentException 如果图书不存在或参数无效
     * @throws RuntimeException 如果数据库操作失败
     */
    void update(Book book);

    /**
     * 删除图书
     *
     * @param isbn ISBN 编号
     * @throws IllegalArgumentException 如果 ISBN 为空
     * @throws RuntimeException 如果数据库操作失败
     */
    void delete(String isbn);

    /**
     * 检查 ISBN 是否已存在
     *
     * @param isbn ISBN 编号
     * @return 如果存在返回 true
     */
    boolean existsByIsbn(String isbn);
}
