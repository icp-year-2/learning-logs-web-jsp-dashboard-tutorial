# Aggregate Queries Reference

## COUNT(*)

Counts the number of rows matching a condition.

```sql
-- Total topics for a user
SELECT COUNT(*) AS count FROM topics WHERE user_id = ?;
```

Returns a single integer — use `rs.getInt("count")` or `rs.getInt(1)` in Java.

## JOIN Aggregates

When data spans multiple tables, use JOIN to connect them before counting.

```sql
-- Total entries for a user (entries don't have user_id directly)
SELECT COUNT(*) AS count
FROM entries e
JOIN topics t ON e.topic_id = t.id
WHERE t.user_id = ?;
```

**Why JOIN?** Entries belong to topics, and topics belong to users. To count a user's entries, we join through the topics table.

## Date Functions

### CURDATE()

Returns today's date (no time component): `2026-04-26`

```sql
-- Entries created today
SELECT COUNT(*) AS count
FROM entries e
JOIN topics t ON e.topic_id = t.id
WHERE t.user_id = ? AND DATE(e.created_at) = CURDATE();
```

`DATE(e.created_at)` strips the time from the timestamp so it matches `CURDATE()`.

### WEEK() and YEAR()

`WEEK(date)` returns the week number (0-53). `YEAR(date)` returns the four-digit year.

```sql
-- Topics created this week
SELECT COUNT(*) AS count
FROM topics
WHERE user_id = ?
  AND WEEK(created_at) = WEEK(CURDATE())
  AND YEAR(created_at) = YEAR(CURDATE());
```

**Why both WEEK and YEAR?** `WEEK(created_at) = WEEK(CURDATE())` alone would match the same week number from any year. Adding the `YEAR()` check ensures we only count the current year.

### DATE_SUB()

Subtracts an interval from a date. Useful for "last N days" queries.

```sql
-- Entries from the last 7 days
SELECT * FROM entries
WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 6 DAY);
```

`INTERVAL 6 DAY` gives 7 days total (today + 6 previous days).

## GROUP BY

Groups rows that share a value, then applies aggregate functions to each group.

```sql
-- Count entries per day for the last 7 days
SELECT DATE(e.created_at) AS entry_date, COUNT(*) AS entry_count
FROM entries e
JOIN topics t ON e.topic_id = t.id
WHERE t.user_id = ?
  AND e.created_at >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)
GROUP BY entry_date
ORDER BY entry_date;
```

Each unique `entry_date` becomes one row in the result with its count.

## HAVING vs WHERE

- `WHERE` filters rows **before** grouping
- `HAVING` filters groups **after** aggregation

```sql
-- Only show days with more than 2 entries
SELECT DATE(created_at) AS d, COUNT(*) AS c
FROM entries
GROUP BY d
HAVING c > 2;
```

## Reading Aggregate Results in Java

Aggregate queries return a single row (or grouped rows). Use `rs.getInt()`:

```java
ResultSet rs = statement.executeQuery();
if (rs.next()) {
    int count = rs.getInt("count");
}
```

For grouped results, iterate with `while (rs.next())` like any multi-row query.
