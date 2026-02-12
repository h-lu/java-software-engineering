import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 泛型类型安全测试类
 * 测试泛型的编译期类型检查和运行时行为
 */
@DisplayName("泛型类型安全测试")
public class GenericsTest {

    // ========== 正例测试：正确使用泛型 ==========

    @Test
    @DisplayName("泛型列表应确保类型安全")
    void shouldEnsureTypeSafetyWithGenericList() {
        // Given - 使用泛型声明
        List<Book> books = new ArrayList<>();

        // When - 添加正确类型的元素
        books.add(new Book("Java Book", "Author", "ISBN-1"));

        // Then - 获取时无需强制类型转换，类型安全
        Book book = books.get(0);
        assertNotNull(book);
        assertEquals("Java Book", book.getTitle());
    }

    @Test
    @DisplayName("泛型 Map 应确保键值类型安全")
    void shouldEnsureTypeSafetyWithGenericMap() {
        // Given - 使用泛型声明
        HashMap<String, Book> bookMap = new HashMap<>();

        // When
        bookMap.put("978-111", new Book("Java 核心技术", "Cay", "978-111"));

        // Then - 获取时类型正确
        Book book = bookMap.get("978-111");
        assertNotNull(book);
        assertEquals("Java 核心技术", book.getTitle());
    }

    @Test
    @DisplayName("菱形运算符 <> 应推断正确类型")
    void shouldInferTypeWithDiamondOperator() {
        // Given - 使用菱形运算符（Java 7+）
        ArrayList<Book> books = new ArrayList<>();
        HashMap<String, Book> bookMap = new HashMap<>();

        // When & Then - 类型推断正确
        books.add(new Book("Book", "Author", "ISBN"));
        bookMap.put("key", new Book("Book2", "Author2", "ISBN2"));

        assertEquals(1, books.size());
        assertEquals(1, bookMap.size());
    }

    // ========== 反例测试：原始类型的风险 ==========

    @Test
    @DisplayName("原始类型允许添加任意对象导致运行时异常")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void rawTypeAllowsAnyObjectAndCausesRuntimeException() {
        // Given - 使用原始类型（不推荐！）
        ArrayList rawList = new ArrayList();

        // When - 可以添加任意类型（这是危险的）
        rawList.add(new Book("Book 1", "Author", "ISBN-1"));
        rawList.add("这是一个字符串");
        rawList.add(123);

        // Then - 遍历时会导致 ClassCastException
        assertThrows(ClassCastException.class, () -> {
            for (Object obj : rawList) {
                // 试图将所有元素当作 Book 处理
                Book book = (Book) obj;
                System.out.println(book.getTitle());
            }
        });
    }

    @Test
    @DisplayName("原始类型 Map 同样存在类型安全风险")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void rawTypeMapHasTypeSafetyIssues() {
        // Given - 使用原始类型
        HashMap rawMap = new HashMap();

        // When - 可以放入任意类型
        rawMap.put("key1", new Book("Book", "Author", "ISBN"));
        rawMap.put(123, "这不是一本书");  // key 和 value 都错了

        // Then - 获取时需要强制转换，可能出错
        Object value = rawMap.get(123);
        assertThrows(ClassCastException.class, () -> {
            Book book = (Book) value;
        });
    }

    // ========== 泛型方法测试 ==========

    @Test
    @DisplayName("泛型方法应能处理不同类型")
    void genericMethodShouldHandleDifferentTypes() {
        // Given
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");

        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);

        // When & Then - 泛型方法可以处理两种类型
        assertEquals(2, countElements(strings));
        assertEquals(3, countElements(integers));
    }

    @Test
    @DisplayName("类型参数边界应限制可接受的类型")
    void typeParameterBoundsShouldRestrictTypes() {
        // Given
        List<Number> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2.5);
        numbers.add(3L);

        // When & Then - 可以计算总和
        double sum = sumOfNumbers(numbers);
        assertEquals(6.5, sum, 0.001);
    }

    // ========== 泛型与子类型关系测试 ==========

    @Test
    @DisplayName("List<Book> 不是 List<Object> 的子类型")
    void listOfBookIsNotSubtypeOfListOfObject() {
        // 这是 Java 泛型的重要特性：不变性（invariance）
        // List<Book> 不能赋值给 List<Object>

        List<Book> books = new ArrayList<>();
        books.add(new Book("Book", "Author", "ISBN"));

        // 以下代码如果取消注释会导致编译错误：
        // List<Object> objects = books;  // ❌ 编译错误

        // 但可以使用通配符
        List<?> wildcards = books;  // ✅ 这是允许的
        assertNotNull(wildcards);
        assertEquals(1, wildcards.size());
    }

    @Test
    @DisplayName("通配符应允许读取但限制写入")
    void wildcardShouldAllowReadButRestrictWrite() {
        // Given
        List<Book> books = new ArrayList<>();
        books.add(new Book("Book 1", "Author", "ISBN-1"));
        books.add(new Book("Book 2", "Author", "ISBN-2"));

        // When - 使用通配符读取
        int count = countAnyList(books);

        // Then
        assertEquals(2, count);
    }

    // ========== 边界测试 ==========

    @Test
    @DisplayName("泛型类型擦除应导致运行时类型信息丢失")
    void typeErasureShouldRemoveRuntimeTypeInfo() {
        // Given
        ArrayList<Book> books = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();

        // Then - 运行时类型信息被擦除
        assertEquals(books.getClass(), strings.getClass());
        assertEquals(ArrayList.class, books.getClass());
    }

    @Test
    @DisplayName("不能创建泛型类型的数组")
    void cannotCreateGenericTypeArray() {
        // 以下代码会导致编译错误：
        // List<String>[] array = new List<String>[10];  // ❌ 编译错误

        // 但可以使用通配符创建
        @SuppressWarnings("unchecked")
        List<?>[] array = new List<?>[10];
        assertNotNull(array);
    }

    // ========== 辅助方法 ==========

    /**
     * 泛型方法：计算列表元素个数
     */
    private <T> int countElements(List<T> list) {
        return list.size();
    }

    /**
     * 带边界的泛型方法：计算数字列表的总和
     */
    private <T extends Number> double sumOfNumbers(List<T> numbers) {
        double sum = 0;
        for (T num : numbers) {
            sum += num.doubleValue();
        }
        return sum;
    }

    /**
     * 使用通配符的方法：可以读取任何类型的列表
     */
    private int countAnyList(List<?> list) {
        int count = 0;
        for (Object obj : list) {
            assertNotNull(obj);
            count++;
        }
        return count;
    }
}
