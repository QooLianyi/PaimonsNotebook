package com.lianyi.paimonsnotebook.common.util.hash

/**
 * 摘要算法
 * from:https://github.com/prasanthj/hasher/blob/master/src/main/java/hasher/com.lianyi.paimonsnotebook.common.util.hash.XXHash.java
 */
class XXHash {
    companion object {
        private const val PRIME64_1 = -0x61c8864e7a143579L
        private const val PRIME64_2 = -0x3d4d51c2d82b14b1L
        private const val PRIME64_3 = 0x165667B19E3779F9L
        private const val PRIME64_4 = -0x7a1435883d4d519dL
        private const val PRIME64_5 = 0x27D4EB2F165667C5L
        private const val DEFAULT_SEED: Long = 0
    }
    /**
     * com.lianyi.paimonsnotebook.common.util.hash.XXHash 64-bit variant.
     *
     * @param data   - input byte array
     * @param length - length of array
     * @param seed   - seed. (default 0)
     * @return - hashcode
     */
    /**
     * com.lianyi.paimonsnotebook.common.util.hash.XXHash 64-bit variant.
     *
     * @param data - input byte array
     * @return - hashcode
     */
    fun hash64(data: ByteArray, length: Int = data.size, seed: Long = DEFAULT_SEED): Long {
        var hash: Long
        var index = 0
        if (length >= 32) {
            var v1 = seed + PRIME64_1 + PRIME64_2
            var v2 = seed + PRIME64_2
            var v3 = seed + 0
            var v4 = seed - PRIME64_1
            val limit = (length - 32).toLong()
            do {
                val k1 = (data[index].toLong() and 0xffL
                        or (data[index + 1].toLong() and 0xffL shl 8)
                        or (data[index + 2].toLong() and 0xffL shl 16)
                        or (data[index + 3].toLong() and 0xffL shl 24)
                        or (data[index + 4].toLong() and 0xffL shl 32)
                        or (data[index + 5].toLong() and 0xffL shl 40)
                        or (data[index + 6].toLong() and 0xffL shl 48)
                        or (data[index + 7].toLong() and 0xffL shl 56))
                v1 = mix(v1, k1)
                index += 8
                val k2 = (data[index].toLong() and 0xffL
                        or (data[index + 1].toLong() and 0xffL shl 8)
                        or (data[index + 2].toLong() and 0xffL shl 16)
                        or (data[index + 3].toLong() and 0xffL shl 24)
                        or (data[index + 4].toLong() and 0xffL shl 32)
                        or (data[index + 5].toLong() and 0xffL shl 40)
                        or (data[index + 6].toLong() and 0xffL shl 48)
                        or (data[index + 7].toLong() and 0xffL shl 56))
                v2 = mix(v2, k2)
                index += 8
                val k3 = (data[index].toLong() and 0xffL
                        or (data[index + 1].toLong() and 0xffL shl 8)
                        or (data[index + 2].toLong() and 0xffL shl 16)
                        or (data[index + 3].toLong() and 0xffL shl 24)
                        or (data[index + 4].toLong() and 0xffL shl 32)
                        or (data[index + 5].toLong() and 0xffL shl 40)
                        or (data[index + 6].toLong() and 0xffL shl 48)
                        or (data[index + 7].toLong() and 0xffL shl 56))
                v3 = mix(v3, k3)
                index += 8
                val k4 = (data[index].toLong() and 0xffL
                        or (data[index + 1].toLong() and 0xffL shl 8)
                        or (data[index + 2].toLong() and 0xffL shl 16)
                        or (data[index + 3].toLong() and 0xffL shl 24)
                        or (data[index + 4].toLong() and 0xffL shl 32)
                        or (data[index + 5].toLong() and 0xffL shl 40)
                        or (data[index + 6].toLong() and 0xffL shl 48)
                        or (data[index + 7].toLong() and 0xffL shl 56))
                v4 = mix(v4, k4)
                index += 8
            } while (index <= limit)
            hash = java.lang.Long.rotateLeft(v1, 1) + java.lang.Long.rotateLeft(v2, 7) + java.lang.Long.rotateLeft(
                v3,
                12
            ) + java.lang.Long.rotateLeft(v4, 18)
            hash = update(hash, v1)
            hash = update(hash, v2)
            hash = update(hash, v3)
            hash = update(hash, v4)
        } else {
            hash = seed + PRIME64_5
        }
        hash += length.toLong()

        // tail
        while (index <= length - 8) {
            val tailStart = index
            var k: Long = 0
            var remaining = length - index
            remaining = if (remaining > 8) 8 else remaining
            when (remaining) {
                8 -> {
                    k = k or ((data[tailStart + 7].toInt() and 0xff).toLong() shl 56)
                    k = k or ((data[tailStart + 6].toInt() and 0xff).toLong() shl 48)
                    k = k or ((data[tailStart + 5].toInt() and 0xff).toLong() shl 40)
                    k = k or ((data[tailStart + 4].toInt() and 0xff).toLong() shl 32)
                    k = k or ((data[tailStart + 3].toInt() and 0xff).toLong() shl 24)
                    k = k or ((data[tailStart + 2].toInt() and 0xff).toLong() shl 16)
                    k = k or ((data[tailStart + 1].toInt() and 0xff).toLong() shl 8)
                    k = k or (data[tailStart].toInt() and 0xff).toLong()
                }

                7 -> {
                    k = k or ((data[tailStart + 6].toInt() and 0xff).toLong() shl 48)
                    k = k or ((data[tailStart + 5].toInt() and 0xff).toLong() shl 40)
                    k = k or ((data[tailStart + 4].toInt() and 0xff).toLong() shl 32)
                    k = k or ((data[tailStart + 3].toInt() and 0xff).toLong() shl 24)
                    k = k or ((data[tailStart + 2].toInt() and 0xff).toLong() shl 16)
                    k = k or ((data[tailStart + 1].toInt() and 0xff).toLong() shl 8)
                    k = k or (data[tailStart].toInt() and 0xff).toLong()
                }

                6 -> {
                    k = k or ((data[tailStart + 5].toInt() and 0xff).toLong() shl 40)
                    k = k or ((data[tailStart + 4].toInt() and 0xff).toLong() shl 32)
                    k = k or ((data[tailStart + 3].toInt() and 0xff).toLong() shl 24)
                    k = k or ((data[tailStart + 2].toInt() and 0xff).toLong() shl 16)
                    k = k or ((data[tailStart + 1].toInt() and 0xff).toLong() shl 8)
                    k = k or (data[tailStart].toInt() and 0xff).toLong()
                }

                5 -> {
                    k = k or ((data[tailStart + 4].toInt() and 0xff).toLong() shl 32)
                    k = k or ((data[tailStart + 3].toInt() and 0xff).toLong() shl 24)
                    k = k or ((data[tailStart + 2].toInt() and 0xff).toLong() shl 16)
                    k = k or ((data[tailStart + 1].toInt() and 0xff).toLong() shl 8)
                    k = k or (data[tailStart].toInt() and 0xff).toLong()
                }

                4 -> {
                    k = k or ((data[tailStart + 3].toInt() and 0xff).toLong() shl 24)
                    k = k or ((data[tailStart + 2].toInt() and 0xff).toLong() shl 16)
                    k = k or ((data[tailStart + 1].toInt() and 0xff).toLong() shl 8)
                    k = k or (data[tailStart].toInt() and 0xff).toLong()
                }

                3 -> {
                    k = k or ((data[tailStart + 2].toInt() and 0xff).toLong() shl 16)
                    k = k or ((data[tailStart + 1].toInt() and 0xff).toLong() shl 8)
                    k = k or (data[tailStart].toInt() and 0xff).toLong()
                }

                2 -> {
                    k = k or ((data[tailStart + 1].toInt() and 0xff).toLong() shl 8)
                    k = k or (data[tailStart].toInt() and 0xff).toLong()
                }

                1 -> k = k or (data[tailStart].toInt() and 0xff).toLong()
            }
            hash = updateTail(hash, k)
            index += 8
        }
        if (index <= length - 4) {
            val tailStart = index
            var k = 0
            var remaining = length - index
            remaining = if (remaining > 4) 4 else remaining
            when (remaining) {
                4 -> {
                    k = (k.toLong() or ((data[tailStart + 3].toInt() and 0xff).toLong() shl 24)).toInt()
                    k = (k.toLong() or ((data[tailStart + 2].toInt() and 0xff).toLong() shl 16)).toInt()
                    k = (k.toLong() or ((data[tailStart + 1].toInt() and 0xff).toLong() shl 8)).toInt()
                    k = (k.toLong() or (data[tailStart].toInt() and 0xff).toLong()).toInt()
                }

                3 -> {
                    k = (k.toLong() or ((data[tailStart + 2].toInt() and 0xff).toLong() shl 16)).toInt()
                    k = (k.toLong() or ((data[tailStart + 1].toInt() and 0xff).toLong() shl 8)).toInt()
                    k = (k.toLong() or (data[tailStart].toInt() and 0xff).toLong()).toInt()
                }

                2 -> {
                    k = (k.toLong() or ((data[tailStart + 1].toInt() and 0xff).toLong() shl 8)).toInt()
                    k = (k.toLong() or (data[tailStart].toInt() and 0xff).toLong()).toInt()
                }

                1 -> k = (k.toLong() or (data[tailStart].toInt() and 0xff).toLong()).toInt()
            }
            hash = updateTail(hash, k)
            index += 4
        }
        while (index < length) {
            hash = updateTail(hash, data[index])
            index++
        }
        hash = finalShuffle(hash)
        return hash
    }

    private fun mix(current: Long, value: Long): Long {
        return java.lang.Long.rotateLeft(current + value * PRIME64_2, 31) * PRIME64_1
    }

    private fun update(hash: Long, value: Long): Long {
        val temp = hash xor mix(0, value)
        return temp * PRIME64_1 + PRIME64_4
    }

    private fun updateTail(hash: Long, value: Long): Long {
        val temp = hash xor mix(0, value)
        return java.lang.Long.rotateLeft(temp, 27) * PRIME64_1 + PRIME64_4
    }

    private fun updateTail(hash: Long, value: Int): Long {
        val unsigned = value.toLong() and 0xFFFFFFFFL
        val temp = hash xor unsigned * PRIME64_1
        return java.lang.Long.rotateLeft(temp, 23) * PRIME64_2 + PRIME64_3
    }

    private fun updateTail(hash: Long, value: Byte): Long {
        val unsigned = value.toInt() and 0xFF
        val temp = hash xor unsigned * PRIME64_5
        return java.lang.Long.rotateLeft(temp, 11) * PRIME64_1
    }

    private fun finalShuffle(hash: Long): Long {
        var hash = hash
        hash = hash xor (hash ushr 33)
        hash *= PRIME64_2
        hash = hash xor (hash ushr 29)
        hash *= PRIME64_3
        hash = hash xor (hash ushr 32)
        return hash
    }
}