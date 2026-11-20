# Longest Valid Parentheses: Java (Two-Pass Two-Pointer Approach)

## 1. Problem Statement

We are given a string that contains only opening brackets `(` and closing brackets `)`.

Our task is to return the **length of the longest continuous part of the string that forms valid parentheses**.

A valid parentheses section follows two rules:

1. Every opening bracket must have a matching closing bracket.
2. While reading from left to right, we must never have more closing brackets than opening brackets.

Examples:

| Input                | Output | Why?                             |
|----------------------|-------:|----------------------------------|
| `(()`                | 2      | The longest valid part is `()`   |
| `)()())`             | 4      | The longest valid part is `()()` |
| `` (empty string) `` | 0      | There are no brackets to match   |
| `(())`               | 4      | The complete string is valid     |
| `(((`                | 0      | No closing bracket is available  |

**One detail to remember:** a substring must be continuous. We cannot skip a character and join two separate parts.

---

## 2. What makes a substring valid?

Take this string:

```text
) ( ) ( ) )
0 1 2 3 4 5
```

The first `)` cannot be part of a valid section because there is no opening bracket before it.

The section from index `1` to index `4` is:

```text
()()
```

It is continuous and every opening bracket has matching closing bracket. Its length is `4`, so the answer is `4`.

---

## 3. Why use this approach?

A stack-based solution is also a good O(n) solution, but it needs extra space to store indices.

Here, we use a **two-pass counting approach**. We only maintain three variables:

- `left`: number of opening brackets seen in the current section.
- `right`: number of closing brackets seen in the current section.
- `maxLength`: the best valid length found so far.

We scan the string twice:

1. Left to right, to handle sections that have extra closing brackets.
2. Right to left, to handle sections that have extra opening brackets.

This gives us **O(n) time and O(1) extra space**. It is a good fit for the constraint of up to 30,000 characters.

---

## 4. The idea, step by step

### Pass 1: Left to right

For each character:

- If it is `(`, increase `left`.
- If it is `)`, increase `right`.
- If `left == right`, the current section is balanced. Its length is `2 * right`, so update `maxLength`.
- If `right > left`, we have seen a closing bracket that cannot be matched. No valid section can continue across this point, so reset both counters to zero.

Why reset when `right > left`?

For example, in `)()())`, the first character is `)`. A closing bracket cannot start a valid section by itself. We reset the counters and begin counting from the next character.

### Pass 2: Right to left

The first pass alone is not enough.

Consider:

```text
(() 
```

From left to right, the counts finish with two opening brackets and one closing bracket. They never become equal, even though `()` is a valid substring.

So we scan in the opposite direction:

- If it is `(`, increase `left`.
- If it is `)`, increase `right`.
- If `left == right`, update the answer using `2 * left`.
- If `left > right`, there are too many opening brackets for this right-to-left section. Reset both counters.

This second pass finds valid sections that the first pass could not count.

---

### Small note about the method name

The method is `static`, so we can call it directly from `main` without creating a `Solution` object. The method returns an `int` because the question asks for the length, not the substring itself.

---

## 6. Dry run 1 — Input: `)()())`

We will follow the first pass one character at a time.

### Pass 1: Left to right

| Index | Character | `left` | `right` | What happens?         | `maxLength` |
|------:|:---------:|-------:|--------:|-----------------------|------------:|
| 0     | `)`       | 0      | 1       | `right > left`, reset | 0           |
| 1     | `(`       | 1      | 0       | Keep counting         | 0           |
| 2     | `)`       | 1      | 1       | Equal, length = 2     | 2           |
| 3     | `(`       | 2      | 1       | Keep counting         | 2           |
| 4     | `)`       | 2      | 2       | Equal, length = 4     | 4           |
| 5     | `)`       | 2      | 3       | `right > left`, reset | 4           |

At index `4`, the balanced section is `()()`. Its length is `2 * 2 = 4`.

The first pass has already found the answer. The second pass still runs because it is needed for other input patterns.

**Final output: `4`**

### Pass 2: Right to left

The scan starts at index `5` and moves towards index `0`. It does not find a longer valid section, so `maxLength` remains `4`.

---

## 7. Dry run 2 — Input: `(()`

This example shows why the second pass matters.

### Pass 1: Left to right

| Index | Character | `left` | `right` | What happens?        | `maxLength` |
|------:|:---------:|-------:|--------:|----------------------|------------:|
| 0     | `(`       | 1      | 0       | Keep counting        | 0           |
| 1     | `(`       | 2      | 0       | Keep counting        | 0           |
| 2     | `)`       | 2      | 1       | Counts are not equal | 0           |

The first pass returns no update because there are more opening brackets than closing brackets.

### Pass 2: Right to left

| Index | Character | `left` | `right` | What happens?         | `maxLength` |
|------:|:---------:|-------:|--------:|-----------------------|------------:|
| 2     | `)`       | 0      | 1       | Keep counting         | 0           |
| 1     | `(`       | 1      | 1       | Equal, length = 2     | 2           |
| 0     | `(`       | 2      | 1       | `left > right`, reset | 2           |

The second pass finds `()` at indices `1` and `2`.

**Final output: `2`**

---

## 8. Why do we reset the counters in opposite conditions?

This is the key part of the solution.

| Direction     |Reset condition | Meaning                          |
|---------------|----------------|----------------------------------|
| Left to right | `right > left` | There are extra closing brackets |
| Right to left | `left > right` | There are extra opening brackets |

In the first pass, an extra closing bracket makes the current section impossible to balance.

In the second pass, an extra opening bracket makes the current section impossible to balance from the right side.

Resetting lets us start counting a fresh possible section after the invalid boundary.

---

## 9. Complexity analysis

Let `n` be the length of the input string.

### Time complexity: O(n)

- First pass visits each character once: O(n).
- Second pass also visits each character once: O(n).
- Total: O(n) + O(n) = O(2n).
- We ignore the constant factor, so the final time complexity is **O(n)**.

### Extra space complexity: O(1)

We only use a fixed number of variables:

- `left`
- `right`
- `maxLength`
- `i` (the loop index)

We do not create a stack, array, or another data structure whose size grows with the input.

So the extra space complexity is **O(1)**.

---

## 10. Edge cases to test

| Input | Expected output | What it checks              |
|------------|---|--------------------------------------|
| `""`       | 0 | Empty input                          |
| `"("`      | 0 | Only an opening bracket              |
| `")"`      | 0 | Only a closing bracket               |
| `"()"`     | 2 | One valid pair                       |
| `"(())"`   | 4 | Nested valid pairs                   |
| `"()()"`   | 4 | Consecutive valid pairs              |
| `"((("`    | 0 | No closing brackets                  |
| `")))"`    | 0 | No opening brackets                  |
| `"(()"`    | 2 | Extra opening bracket                |
| `")()())"` | 4 | Extra closing brackets on both sides |

The method also handles the maximum input length of 30,000 characters comfortably because it makes only two linear passes.

---

Expected output:

```text
2
4
0
4
0
```

---

## 12. Short explanation

> I used a two-pass counting approach with two counters: `left` for opening brackets and `right` for closing brackets.
>
> First, I scan the string from left to right. Whenever both counts become equal, I calculate the current valid length. If closing brackets become greater than opening brackets, I reset both counters because that section cannot be valid.
>
> Then I scan the string from right to left. This second pass handles cases where there are extra opening brackets, which the first pass may miss.
>
> I keep updating the maximum length and return it at the end.
>
> The solution takes O(n) time and O(1) extra space.

---
