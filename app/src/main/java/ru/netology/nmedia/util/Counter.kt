package ru.netology.nmedia.util

class Counter {
    fun counterConversion(count: Int): String {
        return when (count) {
            in 1..999 -> "$count"
            in 1_000..1_099 -> "${count / 1_000}K"
            in 1_100..9_999 -> "${count / 1_000}.${count / 100 % 10}K"
            in 10_000..999_999 -> "${count / 1_000}K"
            in 1_000_000..1_099_999 -> "${count / 1_000_000}M"
            in 1_100_000..99_999_999 -> "${count / 1_000_000}.${count / 100_000 % 10}M"
            else -> ""
        }

    }
}