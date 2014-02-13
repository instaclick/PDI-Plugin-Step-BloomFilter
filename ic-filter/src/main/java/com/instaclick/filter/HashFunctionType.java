package com.instaclick.filter;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * @author Fabio B. Silva <fabio.bat.silva@gmail.com>
 */
public enum HashFunctionType
{
    NONE(null),
    MD5(Hashing.md5()),
    SHA1(Hashing.sha1()),
    SHA256(Hashing.sha256()),
    SHA512(Hashing.sha512()),
    CRC32(Hashing.crc32()),
    ADLER32(Hashing.adler32()),
    MURMUR3_32(Hashing.murmur3_32()),
    MURMUR3_128(Hashing.murmur3_128()),
    GOOD_FAST_HASH_32(Hashing.goodFastHash(32)),
    GOOD_FAST_HASH_64(Hashing.goodFastHash(64)),
    GOOD_FAST_HASH_128(Hashing.goodFastHash(128)),
    GOOD_FAST_HASH_256(Hashing.goodFastHash(256)),
    GOOD_FAST_HASH_512(Hashing.goodFastHash(512));

    private final HashFunction hashFunction;

    private HashFunctionType(HashFunction hashFunction)
    {
      this.hashFunction = hashFunction;
    }

    public HashFunction getHashFunction()
    {
      return hashFunction;
    }

    public static String[] getHashFunctionNames()
    {
        return new String[]{
            NONE.toString(),
            MD5.toString(),
            SHA1.toString(),
            SHA256.toString(),
            SHA512.toString(),
            MURMUR3_32.toString(),
            MURMUR3_128.toString(),
            GOOD_FAST_HASH_32.toString(),
            GOOD_FAST_HASH_64.toString(),
            GOOD_FAST_HASH_128.toString(),
            GOOD_FAST_HASH_512.toString(),
        };
    }
}