import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ArrayList 测试类
 * 测试 ArrayList 的基本操作、边界情况和异常处理
 */
@DisplayName("ArrayList 基础测试")
public class ArrayListTest {

    private ArrayList<Book> books;

    @BeforeEach
    void setUp() {
        books = new ArrayList<>();
    }

    // ========== 正例测试（Happy Path）==========

    @Test
    @DisplayName("添加元素后列表大小应增加")
    void shouldIncreaseSizeWhenAddingElements() {
        // Given
        Book book1 = new Book("Java 核心技术", "Cay Horstmann", "978-111");
        Book book2 = new Book("Effective Java", "Joshua Bloch", "978-222");

        // When
        books.add(book1);
        books.add(book2);

        // Then
        assertEquals(2, books.size(), "添加两本书后大小应为 2");
    }

    @Test
    @DisplayName("通过索引获取元素应返回正确对象")
    void shouldReturnCorrectElementWhenGettingByIndex() {
        // Given
        Book book = new Book("Clean Code", "Robert Martin", "978-333");
        books.add(book);

        // When
        Book retrieved = books.get(0);

        // Then
        assertEquals(book, retrieved, "获取的元素应与添加的相同");
        assertEquals("Clean Code", retrieved.getTitle());
    }

    @Test
    @DisplayName("在指定位置插入元素应成功")
    void shouldInsertElementAtSpecifiedIndex() {
        // Given
        Book book1 = new Book("Book 1", "Author 1", "ISBN-1");
        Book book2 = new Book("Book 2", "Author 2", "ISBN-2");
        Book book3 = new Book("Book 3", "Author 3", "ISBN-3");
        books.add(book1);
        books.add(book2);

        // When
        books.add(1, book3);  // 在索引 1 处插入

        // Then
        assertEquals(3, books.size());
        assertEquals(book1, books.get(0));
        assertEquals(book3, books.get(1));
        assertEquals(book2, books.get(2));
    }

    @Test
    @DisplayName("删除元素后列表应缩小")
    void shouldDecreaseSizeWhenRemovingElement() {
        // Given
        Book book1 = new Book("Book 1", "Author 1", "ISBN-1");
        Book book2 = new Book("Book 2", "Author 2", "ISBN-2");
        books.add(book1);
        books.add(book2);

        // When
        books.remove(0);

        // Then
        assertEquals(1, books.size());
        assertEquals(book2, books.get(0));
    }

    @Test
    @DisplayName("检查包含关系应正确")
    void shouldCorrectlyCheckContainment() {
        // Given
        Book book1 = new Book("Book 1", "Author 1", "ISBN-1");
        Book book2 = new Book("Book 2", "Author 2", "ISBN-2");
        books.add(book1);

        // Then
        assertTrue(books.contains(book1), "应包含已添加的书");
        assertFalse(books.contains(book2), "不应包含未添加的书");
    }

    @Test
    @DisplayName("清空列表后应为空")
    void shouldBeEmptyAfterClear() {
        // Given
        books.add(new Book("Book 1", "Author 1", "ISBN-1"));
        books.add(new Book("Book 2", "Author 2", "ISBN-2"));

        // When
        books.clear();

        // Then
        assertTrue(books.isEmpty(), "清空后应为空");
        assertEquals(0, books.size());
    }

    @Test
    @DisplayName("增强 for 循环应能遍历所有元素")
    void shouldIterateAllElementsWithEnhancedFor() {
        // Given
        books.add(new Book("Book 1", "Author 1", "ISBN-1"));
        books.add(new Book("Book 2", "Author 2", "ISBN-2"));
        books.add(new Book("Book 3", "Author 3", "ISBN-3"));

        // When
        int count = 0;
        for (Book book : books) {
            assertNotNull(book);
            count++;
        }

        // Then
        assertEquals(3, count);
    }

    @Test
    @DisplayName("使用 Iterator 遍历时删除元素应成功")
    void shouldRemoveElementDuringIterationWithIterator() {
        // Given
        books.add(new Book("Book 1", "佚名", "ISBN-1"));
        books.add(new Book("Book 2", "Author 2", "ISBN-2"));
        books.add(new Book("Book 3", "佚名", "ISBN-3"));

        // When
        Iterator<Book> iterator = books.iterator();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            if ("佚名".equals(book.getAuthor())) {
                iterator.remove();
            }
        }

        // Then
        assertEquals(1, books.size());
        assertEquals("Author 2", books.get(0).getAuthor());
    }

    // ========== 边界测试（Boundary Cases）==========

    @Test
    @DisplayName("空列表的大小应为 0")
    void shouldHaveZeroSizeWhenEmpty() {
        assertEquals(0, books.size(), "新创建的列表大小应为 0");
        assertTrue(books.isEmpty(), "新创建的列表应为空");
    }

    @Test
    @DisplayName("访问索引 0 当列表为空时应抛出异常")
    void shouldThrowExceptionWhenAccessingIndexZeroOfEmptyList() {
        // When & Then
        assertThrows(IndexOutOfBoundsException.class, () -> {
            books.get(0);
        }, "访问空列表的索引 0 应抛出 IndexOutOfBoundsException");
    }

    @Test
    @DisplayName("访问越界索引时应抛出 IndexOutOfBoundsException")
    void shouldThrowExceptionWhenAccessingOutOfBoundsIndex() {
        // Given
        books.add(new Book("Book 1", "Author 1", "ISBN-1"));

        // When & Then
        assertThrows(IndexOutOfBoundsException.class, () -> {
            books.get(100);
        }, "访问越界索引应抛出 IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            books.get(-1);
        }, "访问负索引应抛出 IndexOutOfBoundsException");
    }

    @Test
    @DisplayName("在边界索引处插入元素应成功")
    void shouldInsertAtBoundaryIndices() {
        // Given
        Book book1 = new Book("Book 1", "Author 1", "ISBN-1");
        Book book2 = new Book("Book 2", "Author 2", "ISBN-2");

        // When - 在开头插入
        books.add(0, book1);
        assertEquals(book1, books.get(0));

        // When - 在末尾插入
        books.add(books.size(), book2);
        assertEquals(book2, books.get(1));
    }

    @Test
    @DisplayName("添加大量元素应自动扩容")
    void shouldAutoExpandWhenAddingManyElements() {
        // When - 添加 1000 本书（远超默认初始容量）
        for (int i = 0; i < 1000; i++) {
            books.add(new Book("Book " + i, "Author " + i, "ISBN" + i));
        }

        // Then
        assertEquals(1000, books.size());
        assertEquals("Book 0", books.get(0).getTitle());
        assertEquals("Book 999", books.get(999).getTitle());
    }

    @Test
    @DisplayName("删除最后一个元素后列表为空")
    void shouldBeEmptyAfterRemovingLastElement() {
        // Given
        books.add(new Book("Book 1", "Author 1", "ISBN-1"));

        // When
        books.remove(0);

        // Then
        assertTrue(books.isEmpty());
        assertEquals(0, books.size());
    }

    // ========== 泛型类型安全测试 ==========

    @Test
    @DisplayName("泛型列表应拒绝错误类型的编译期检查")
    @SuppressWarnings("unchecked")
    void shouldBeTypeSafeWithGenerics() {
        // 注意：这是编译期测试，以下代码如果取消注释会导致编译错误
        // books.add("这不是一本书");  // ❌ 编译错误：类型不匹配

        // 使用原始类型演示运行时风险（不推荐）
        ArrayList rawList = new ArrayList();
        rawList.add(new Book("Book 1", "Author 1", "ISBN-1"));
        rawList.add("这是一个字符串");  // 原始类型允许这样做

        // 这会导致 ClassCastException
        assertThrows(ClassCastException.class, () -> {
            for (Object obj : rawList) {
                Book book = (Book) obj;  // 第二个元素会导致异常
            }
        });
    }

    @Test
    @DisplayName("泛型确保获取元素无需强制类型转换")
    void shouldNotRequireCastWithGenerics() {
        // Given
        books.add(new Book("Java Book", "Author", "ISBN-1"));

        // When - 泛型确保返回类型就是 Book，无需 (Book) 强制转换
        Book book = books.get(0);

        // Then
        assertNotNull(book);
        assertEquals("Java Book", book.getTitle());
    }
}
