package io.ethers.bigint

actual typealias BigDecimal = java.math.BigDecimal

actual fun BigInt.toBigDecimal(scale: Int): BigDecimal = java.math.BigDecimal(this, scale)
