package io.ethers.bigint

/**
 * Platform-agnostic arbitrary-precision decimal.
 *
 * On JVM/Android this is an `actual typealias` to [java.math.BigDecimal]. Only the
 * minimal surface used by `EthUnit` is declared here; future consumers should add
 * methods explicitly so the contract stays portable across platforms.
 */
expect class BigDecimal : Comparable<BigDecimal> {
    fun toPlainString(): String

    override fun compareTo(other: BigDecimal): Int
}

/**
 * Returns a [BigDecimal] whose value is `this * 10^(-scale)`.
 *
 * Mirrors `java.math.BigInteger.toBigDecimal(scale: Int)` (Kotlin stdlib extension).
 */
expect fun BigInt.toBigDecimal(scale: Int): BigDecimal
