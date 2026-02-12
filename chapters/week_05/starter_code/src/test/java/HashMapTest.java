import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HashMap 测试类
 * 测试 HashMap 的基本操作、边界情况和 null 处理
 */
@DisplayName("HashMap 基础测试")
public class HashMapTest {

    private HashMap<String, Book> bookMap;

    @BeforeEach
    void setUp() {
        bookMap = new HashMap<>();
    }

    // ========== 正例测试（Happy Path）==========

    @Test
    @DisplayName("put 后应能通过 key 获取 value")
    void shouldRetrieveValueAfterPut() {
        // Given
        String isbn = "978-111";
        Book book = new Book("Java 核心技术", "Cay Horstmann", isbn);

        // When
        bookMap.put(isbn, book);

        // Then
        Book retrieved = bookMap.get(isbn);
        assertNotNull(retrieved);
        assertEquals(book, retrieved);
        assertEquals("Java 核心技术", retrieved.getTitle());
    }

    @Test
    @DisplayName("containsKey 应正确判断 key 是否存在")
    void shouldCorrectlyCheckKeyExistence() {
        // Given
        bookMap.put("ISBN-1", new Book("Book 1", "Author 1", "ISBN-1"));

        // Then
        assertTrue(bookMap.containsKey("ISBN-1"), "应包含已添加的 key");
        assertFalse(bookMap.containsKey("ISBN-NOT-EXIST"), "不应包含未添加的 key");
    }

    @Test
    @DisplayName("containsValue 应正确判断 value 是否存在")
    void shouldCorrectlyCheckValueExistence() {
        // Given
        Book book = new Book("Book 1", "Author 1", "ISBN-1");
        bookMap.put("ISBN-1", book);

        // Then
        assertTrue(bookMap.containsValue(book), "应包含已添加的 value");
        assertFalse(bookMap.containsValue(new Book("Book 2", "Author 2", "ISBN-2")),
            "不应包含未添加的 value");
    }

    @Test
    @DisplayName("相同 key 再次 put 应覆盖旧值")
    void shouldOverrideOldValueWhenPuttingSameKey() {
        // Given
        String isbn = "978-111";
        Book oldBook = new Book("Old Title", "Old Author", isbn);
        Book newBook = new Book("New Title", "New Author", isbn);

        // When
        bookMap.put(isbn, oldBook);
        Book previous = bookMap.put(isbn, newBook);

        // Then
        assertEquals(oldBook, previous, "put 应返回旧值");
        assertEquals(newBook, bookMap.get(isbn), "新值应覆盖旧值");
        assertEquals(1, bookMap.size(), "大小应保持为 1");
    }

    @Test
    @DisplayName("remove 应删除指定 key 的条目")
    void shouldRemoveEntryWhenRemoveCalled() {
        // Given
        String isbn = "978-111";
        Book book = new Book("Java Book", "Author", isbn);
        bookMap.put(isbn, book);

        // When
        Book removed = bookMap.remove(isbn);

        // Then
        assertEquals(book, removed, "remove 应返回被删除的值");
        assertNull(bookMap.get(isbn), "删除后应无法获取");
        assertFalse(bookMap.containsKey(isbn));
    }

    @Test
    @DisplayName("clear 应清空所有条目")
    void shouldClearAllEntries() {
        // Given
        bookMap.put("ISBN-1", new Book("Book 1", "Author 1", "ISBN-1"));
        bookMap.put("ISBN-2", new Book("Book 2", "Author 2", "ISBN-2"));

        // When
        bookMap.clear();

        // Then
        assertTrue(bookMap.isEmpty());
        assertEquals(0, bookMap.size());
    }

    @Test
    @DisplayName("keySet 应返回所有 key")
    void shouldReturnAllKeys() {
        // Given
        bookMap.put("ISBN-1", new Book("Book 1", "Author 1", "ISBN-1"));
        bookMap.put("ISBN-2", new Book("Book 2", "Author 2", "ISBN-2"));

        // When
        var keys = bookMap.keySet();

        // Then
        assertEquals(2, keys.size());
        assertTrue(keys.contains("ISBN-1"));
        assertTrue(keys.contains("ISBN-2"));
    }

    @Test
    @DisplayName("values 应返回所有 value")
    void shouldReturnAllValues() {
        // Given
        Book book1 = new Book("Book 1", "Author 1", "ISBN-1");
        Book book2 = new Book("Book 2", "Author 2", "ISBN-2");
        bookMap.put("ISBN-1", book1);
        bookMap.put("ISBN-2", book2);

        // When
        var values = bookMap.values();

        // Then
        assertEquals(2, values.size());
        assertTrue(values.contains(book1));
        assertTrue(values.contains(book2));
    }

    @Test
    @DisplayName("entrySet 应返回所有键值对")
    void shouldReturnAllEntries() {
        // Given
        bookMap.put("ISBN-1", new Book("Book 1", "Author 1", "ISBN-1"));
        bookMap.put("ISBN-2", new Book("Book 2", "Author 2", "ISBN-2"));

        // When
        int count = 0;
        for (Map.Entry<String, Book> entry : bookMap.entrySet()) {
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
            count++;
        }

        // Then
        assertEquals(2, count);
    }

    // ========== 边界测试（Boundary Cases）==========

    @Test
    @DisplayName("空 Map 的大小应为 0")
    void shouldHaveZeroSizeWhenEmpty() {
        assertEquals(0, bookMap.size());
        assertTrue(bookMap.isEmpty());
    }

    @Test
    @DisplayName("获取不存在的 key 应返回 null")
    void shouldReturnNullWhenGettingNonExistentKey() {
        // When
        Book result = bookMap.get("NOT-EXIST");

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("删除不存在的 key 应返回 null")
    void shouldReturnNullWhenRemovingNonExistentKey() {
        // When
        Book result = bookMap.remove("NOT-EXIST");

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("put null key 应成功")
    void shouldAllowNullKey() {
        // Given
        Book book = new Book("Book", "Author", "ISBN");

        // When
        bookMap.put(null, book);

        // Then
        assertEquals(book, bookMap.get(null));
        assertTrue(bookMap.containsKey(null));
    }

    @Test
    @DisplayName("put null value 应成功")
    void shouldAllowNullValue() {
        // When
        bookMap.put("ISBN-1", null);

        // Then
        assertNull(bookMap.get("ISBN-1"));
        assertTrue(bookMap.containsKey("ISBN-1"));
        // 注意：containsValue(null) 会返回 true，但需要小心使用
    }

    @Test
    @DisplayName("put 大量元素应正常工作")
    void shouldHandleLargeNumberOfEntries() {
        // When
        for (int i = 0; i < 10000; i++) {
            bookMap.put("ISBN" + i, new Book("Book " + i, "Author " + i, "ISBN" + i));
        }

        // Then
        assertEquals(10000, bookMap.size());
        assertNotNull(bookMap.get("ISBN5000"));
    }

    @Test
    @DisplayName("空字符串作为 key 应正常工作")
    void shouldAllowEmptyStringAsKey() {
        // Given
        Book book = new Book("Book", "Author", "ISBN");

        // When
        bookMap.put("", book);

        // Then
        assertEquals(book, bookMap.get(""));
    }

    // ========== 性能相关测试（说明 HashMap 的 O(1) 特性）==========

    @Test
    @DisplayName("查找时间应与数据量无关（O(1) 特性）")
    void shouldHaveConstantTimeLookup() {
        // Given - 准备大量数据
        for (int i = 0; i < 100000; i++) {
            bookMap.put("ISBN" + i, new Book("Book " + i, "Author", "ISBN" + i));
        }

        // When - 查找特定元素（无论 Map 多大，都应快速返回）
        long startTime = System.nanoTime();
        Book result = bookMap.get("ISBN50000");
        long endTime = System.nanoTime();

        // Then
        assertNotNull(result);
        // 查找应在微秒级别完成（这里给一个很宽松的上限）
        long durationMicros = (endTime - startTime) / 1000;
        assertTrue(durationMicros < 1000, "查找应在 1 毫秒内完成，实际耗时: " + durationMicros + " 微秒");
    }
}
