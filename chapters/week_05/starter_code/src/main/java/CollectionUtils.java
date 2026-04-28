import java.util.List;
import java.util.Map;

public class CollectionUtils {
    private CollectionUtils() {
    }

    public static <T, K> Map<K, List<T>> groupBy(List<T> list, KeyExtractor<T, K> extractor) {
        // TODO: 遍历 list，用 extractor 提取 key，并把相同 key 的元素放到同一个 List。
        throw new UnsupportedOperationException("TODO: implement groupBy");
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        // TODO: 返回所有满足 predicate 的元素；null list 应返回空列表。
        throw new UnsupportedOperationException("TODO: implement filter");
    }

    public static <T> T findFirst(List<T> list, Predicate<T> predicate) {
        // TODO: 返回第一个满足 predicate 的元素；找不到或 list 为 null 时返回 null。
        throw new UnsupportedOperationException("TODO: implement findFirst");
    }

    @FunctionalInterface
    public interface KeyExtractor<T, K> {
        K extract(T item);
    }

    @FunctionalInterface
    public interface Predicate<T> {
        boolean test(T item);
    }
}
