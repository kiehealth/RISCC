package com.cronelab.riscc.support.common.utils

import java.math.BigInteger
import java.util.*
import java.util.regex.Pattern


class NumberAwareStringComparator : Comparator<String> {

    companion object {
        val INSTANCE = NumberAwareStringComparator()
        private val PATTERN = Pattern.compile("(\\D*)(\\d*)")
    }

    override fun compare(s1: String, s2: String): Int {
        val m1 = PATTERN.matcher(s1)
        val m2 = PATTERN.matcher(s2)

        // The only way find() could fail is at the end of a string
        while (m1.find() && m2.find()) {
            // matcher.group(1) fetches any non-digits captured by the
            // first parentheses in PATTERN.
            val nonDigitCompare = m1.group(1).compareTo(m2.group(1))
            if (0 != nonDigitCompare) {
                return nonDigitCompare
            }

            // matcher.group(2) fetches any digits captured by the
            // second parentheses in PATTERN.
            if (m1.group(2).isEmpty()) {
                return if (m2.group(2).isEmpty()) 0 else -1
            } else if (m2.group(2).isEmpty()) {
                return +1
            }
            val n1 = BigInteger(m1.group(2))
            val n2 = BigInteger(m2.group(2))
            val numberCompare = n1.compareTo(n2)
            if (0 != numberCompare) {
                return numberCompare
            }
        }

        // Handle if one string is a prefix of the other.
        // Nothing comes before something.
        return if (m1.hitEnd() && m2.hitEnd()) 0 else if (m1.hitEnd()) -1 else +1
    }

}