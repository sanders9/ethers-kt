package io.ethers.bigint

actual typealias BigInt = java.math.BigInteger

actual object BigInts {
    actual val ZERO: BigInt = java.math.BigInteger.ZERO
    actual val ONE: BigInt = java.math.BigInteger.ONE
    actual val TWO: BigInt = java.math.BigInteger.TWO
    actual val TEN: BigInt = java.math.BigInteger.TEN

    actual fun valueOf(value: Long): BigInt = java.math.BigInteger.valueOf(value)

    actual fun fromUnsignedBytes(
        bytes: ByteArray,
        offset: Int,
        length: Int,
    ): BigInt = java.math.BigInteger(1, bytes, offset, length)

    actual fun fromSignedBytes(
        bytes: ByteArray,
        offset: Int,
        length: Int,
    ): BigInt = java.math.BigInteger(bytes, offset, length)
}
