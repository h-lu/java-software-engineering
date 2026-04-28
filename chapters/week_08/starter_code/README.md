# Week 08 Starter Code: Refactoring and Strategy Pattern

This starter gives you a deliberately smelly `OrderProcessor` plus empty strategy skeletons. Your job is to refactor safely, not to copy a finished design.

## Run

```bash
cd chapters/week_08/starter_code
mvn test
```

The active smoke test is a characterization test: it records one known behavior before you refactor. Keep it passing while you improve the design.

## Files To Edit

- `src/main/java/com/campusflow/OrderProcessor.java`
- Add extracted classes such as `PriceCalculator`, `ShippingCalculator`, or payment helpers.
- `src/main/java/com/campusflow/*DiscountStrategy.java`
- `src/main/java/com/campusflow/DiscountStrategyFactory.java`
- Add or enable tests under `src/test/java/com/campusflow/`

## TODO Checklist

- Identify at least five code smells in the starting `OrderProcessor`.
- Add characterization tests before changing behavior.
- Extract price, shipping, payment, and persistence responsibilities.
- Implement `DiscountStrategy` classes and the factory.
- Enable and complete the disabled TODO tests.
- Document your refactoring decisions and ADR-004 in your own submission repo.
