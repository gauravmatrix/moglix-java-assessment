//Longest Valid Parentheses: Java (Two-Pass Two-Pointer Approach)

public class LongestValidParentheses {

    public static int longestValidParentheses(String s) {
        // These counters help us keep track of opening and closing brackets.
        int left = 0;
        int right = 0;
        int maxLength = 0;

        // Pass 1: Read the string from left to right.
        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);

            if (current == '(') {
                left++;
            } else {
                right++;
            }

            // Equal counts mean the current part is balanced.
            if (left == right) {
                maxLength = Math.max(maxLength, 2 * right);
            }
            // Too many closing brackets: this part cannot become valid.
            else if (right > left) {
                left = 0;
                right = 0;
            }
        }

        // Start fresh before checking the other direction.
        left = 0;
        right = 0;

        // Pass 2: Read from right to left.
        // This catches cases with extra opening brackets, such as "(()".
        for (int i = s.length() - 1; i >= 0; i--) {
            char current = s.charAt(i);

            if (current == '(') {
                left++;
            } else {
                right++;
            }

            // Equal counts give us a balanced substring.
            if (left == right) {
                maxLength = Math.max(maxLength, 2 * left);
            }
            // Too many opening brackets: reset and start a new section.
            else if (left > right) {
                left = 0;
                right = 0;
            }
        }

        return maxLength;
    }

    public static void main(String[] args) {
        // A few test cases to check normal and edge-case inputs.
        System.out.println(longestValidParentheses("(()"));      // 2
        System.out.println(longestValidParentheses(")()())"));   // 4
        System.out.println(longestValidParentheses(""));         // 0
        System.out.println(longestValidParentheses("(())"));     // 4
        System.out.println(longestValidParentheses("((("));      // 0
    }
}
