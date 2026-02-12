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
