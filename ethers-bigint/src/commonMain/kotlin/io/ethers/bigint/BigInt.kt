package io.ethers.bigint

/**
 * Platform-agnostic arbitrary-precision integer.
 *
 * On JVM and Android targets this is an `actual typealias` to [java.math.BigInteger], so
 * usage is zero-overhead on those platforms. On future native targets (e.g. iOS) the actual
 * implementation will be backed by `com.ionspin.kotlin:bignum` (or a `secp256k1` GMP cinterop).
 *
 * Only the API surface used by the ethers-kt modules is declared here; this is intentionally
 * **not** a full mirror of `java.math.BigInteger`.
 */
expect class BigInt : Comparable<BigInt> {
    constructor(value: String)
    constructor(value: String, radix: Int)

    fun add(other: BigInt): BigInt
    fun subtract(other: BigInt): BigInt
    fun multiply(other: BigInt): BigInt
    fun divide(other: BigInt): BigInt
    fun negate(): BigInt
    fun pow(exponent: Int): BigInt
    fun signum(): Int
    fun bitLength(): Int

    /**
     * Returns the two's-complement big-endian byte representation of this value, matching
     * the contract of [java.math.BigInteger.toByteArray]: a sign byte may be present, the
     * minimum number of bytes is used, and the array length is always >= 1.
     */
    fun toByteArray(): ByteArray

    fun toString(radix: Int): String

    override fun compareTo(other: BigInt): Int
}

/**
 * Companion-style factories and constants for [BigInt].
 *
 * A separate `expect object` is used (rather than a companion object on [BigInt]) because
 * `actual typealias BigInt = java.math.BigInteger` cannot synthesize an `actual` companion
 * object for a Java class that does not declare one.
 */
expect object BigInts {
    val ZERO: BigInt
    val ONE: BigInt
    val TWO: BigInt
    val TEN: BigInt

    fun valueOf(value: Long): BigInt

    /**
     * Builds a non-negative [BigInt] from the unsigned big-endian byte slice
     * `bytes[offset, offset + length)`. Equivalent to `java.math.BigInteger(1, bytes, offset, length)`.
     */
    fun fromUnsignedBytes(
        bytes: ByteArray,
        offset: Int = 0,
        length: Int = bytes.size - offset,
    ): BigInt

    /**
     * Builds a [BigInt] from the two's-complement big-endian byte slice
     * `bytes[offset, offset + length)`. Equivalent to `java.math.BigInteger(bytes, offset, length)`.
     */
    fun fromSignedBytes(
        bytes: ByteArray,
        offset: Int = 0,
        length: Int = bytes.size - offset,
    ): BigInt
}

fun BigInt.min(other: BigInt): BigInt = if (this <= other) this else other

fun BigInt.max(other: BigInt): BigInt = if (this >= other) this else other

fun Int.toBigInt(): BigInt = BigInts.valueOf(this.toLong())

fun Long.toBigInt(): BigInt = BigInts.valueOf(this)

fun String.toBigInt(): BigInt = BigInt(this)

fun String.toBigInt(radix: Int): BigInt = BigInt(this, radix)
