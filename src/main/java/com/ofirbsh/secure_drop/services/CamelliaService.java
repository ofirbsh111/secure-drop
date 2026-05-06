package com.ofirbsh.secure_drop.services;

import org.springframework.stereotype.Service;

@Service
public class CamelliaService 
{
    private static final int[] SBOX1 = new int[] {
        112, 130, 44, 236, 179, 39, 192, 229, 228, 133, 87, 53, 234, 12, 174, 65,
        35, 239, 107, 147, 69, 25, 165, 33, 237, 14, 79, 78, 29, 101, 146, 189,
        134, 184, 175, 143, 124, 235, 31, 206,  62, 48, 220,  95, 94, 197,  11, 26,
        166, 225,  57, 202, 213,  71, 93, 61, 217, 1, 90, 214, 81, 86, 108, 77,
        139, 13, 154, 102, 251, 204, 176, 45, 116, 18, 43, 32, 240, 177, 132, 153,
        223, 76, 203, 194, 52, 126, 118, 5, 109, 183, 169, 49, 209, 23, 4, 215,
        20, 88, 58, 97, 222, 27, 17, 28, 50, 15, 156, 22, 83, 24, 242, 34,
        254, 68, 207, 178, 195, 181, 122, 145, 36, 8, 232, 168, 96, 252, 105,  80,
        170, 208, 160, 125, 161, 137,  98, 151, 84, 91, 30, 149, 224, 255, 100, 210,
        16, 196, 0, 72, 163, 247, 117, 219, 138, 3, 230, 218, 9, 63, 221, 148,
        135, 92, 131, 2, 205, 74, 144, 51, 115, 103, 246, 243, 157, 127, 191, 226,
        82, 155, 216, 38, 200, 55, 198, 59, 129, 150, 111, 75, 19, 190,  99, 46,
        233, 121, 167, 140, 159, 110, 188, 142, 41, 245, 249, 182, 47, 253, 180, 89,
        120, 152, 6, 106, 231,  70, 113, 186, 212, 37, 171, 66, 136, 162, 141, 250,
        114, 7, 185, 85, 248, 238, 172, 10, 54, 73, 42, 104, 60, 56, 241, 164,
        64, 40, 211, 123, 187, 201, 67, 193, 21, 227, 173, 244, 119, 199, 128, 158 };

    // Main Fucntions
    public static byte[] encrypt(byte[] data, byte[] key)
    {
        byte[] padded = addPadding(data);
        byte[][] blocks = splitToBlocks(padded);

        for (int i = 0; i < blocks.length; i++)
            blocks[i] = encryptBlock(blocks[i], key);

        byte[] finalData = joinBlocks(blocks);

        return finalData;
    }

    public byte[] decrypt(byte[] data, byte[] key)
    {
        byte[][] blocks = splitToBlocks(data);

        for (int i = 0; i < blocks.length; i++)
            blocks[i] = decryptBlock(blocks[i], key);

        byte[] finalData = joinBlocks(blocks);

        return removePadding(finalData);
    }

    // Core Function
    /**
     * Take the right side of a byte and encrypt it with sbox
     * @param right
     * @param subKey
     * @return
     */
    public static byte[] F(byte[] right, byte[] subKey)
    {
        byte[] result = new byte[8];

        for (int i = 0; i < result.length; i++)
        {
            int x = (right[i] ^ subKey[i]) & 0xFF;

            switch (i % 4)
            {
                case 0: x = sbox1(x); break;
                case 1: x = sbox2(x); break;
                case 2: x = sbox3(x); break;
                case 3: x = sbox4(x); break;
            }



            result[i] = (byte) x;
        }

        return P(result);
    }

    /**
     * Diffusion inside the right side of the block using XOR
     * @param Block
     * @return
     */
    public static byte[] P(byte[] Block)
    {
        byte[] result = new byte[8];

        result[0] = (byte) (Block[0] ^ Block[3] ^ Block[5]);
        result[1] = (byte) (Block[1] ^ Block[4]);
        result[2] = (byte) (Block[2] ^ Block[6] ^ Block[7]);
        result[3] = (byte) (Block[0] ^ Block[3] ^ Block[6]);
        result[4] = (byte) (Block[1] ^ Block[4] ^ Block[7]);
        result[5] = (byte) (Block[2] ^ Block[5]);
        result[6] = (byte) (Block[3] ^ Block[6]);
        result[7] = (byte) (Block[4] ^ Block[5] ^ Block[7]);

        return result;
    }

    /**
     * Genarate sub keys from a key
     * @param key
     * @param rounds how many keys are needed (rounds = subkeys)
     * @return Generated Subkeys
     */
    public static byte[][] genetateSubKeys(byte[] key, int rounds)
    {
        byte[][] subKeys = new byte[rounds][8];

        for (int r = 0; r < rounds; r++) 
        {
            for (int i = 0; i < subKeys.length; i++)
                subKeys[r][i] = key[(i + r) % key.length];
        }

        return subKeys;
    }

    /**
     * Reverse the subkeys
     * @param subKeys
     * @return
     */
    public byte[][] reverseSubKeys(byte[][] subKeys)
    {
        byte[][] reversed = new byte[subKeys.length][8];

        for (int i = 0; i < subKeys.length; i++)
            reversed[i] = subKeys[subKeys.length - 1 - i];

        return reversed;
    }

    /**
     * Diffustion the block based on the rounds
     * @param left
     * @param right
     * @param rounds
     * @return encrypted block
     */
    public static byte[] feistelRounds(byte[] left, byte[] right, byte[][] subKeys)
    {
        for (int r = 0; r < subKeys.length; r++) 
        {
            byte[] subKey = subKeys[r];

            byte[] newLeft = right;
            byte[] newRight = new byte[8];

            byte[] fResult = F(right, subKey);
            for (int i = 0; i < newRight.length; i++)
                newRight[i] = (byte) (left[i] ^ fResult[i]);

            left = newLeft;
            right = newRight;
        }

        byte[] result = new byte[16];

        for (int i = 0; i < left.length; i++)
            result[i] = left[i];
        
        for (int i = 0; i < right.length; i++)
            result[i+8] = right[i];


        return result;
    }

    /**
     * Decrypt the block based on the rounds
     * @param left
     * @param right
     * @param rounds
     * @return Decrypted block
     */
    public byte[] feistelRoundsDecrypt(byte[] left, byte[] right, byte[][] subKeys)
    {
        for (int r = 0; r < subKeys.length; r++) 
        {
            byte[] subKey = subKeys[r];

            byte[] oldRight = left;
            byte[] oldLeft = new byte[8];

            byte[] fResult = F(oldRight, subKey);

            for (int i = 0; i < oldLeft.length; i++)
                oldLeft[i] = (byte) (right[i] ^ fResult[i]);

            left = oldLeft;
            right = oldRight;
        }

        byte[] result = new byte[16];

        for (int i = 0; i < left.length; i++)
            result[i] = left[i];
        
        for (int i = 0; i < right.length; i++)
            result[i+8] = right[i];


        return result;
    }

    /**
     * Encrypt one block
     * @param block
     * @param key
     * @return
     */
    public static byte[] encryptBlock(byte[] block, byte[] key)
    {
        byte[] whitened = xorBlock(block, key);

        byte[] left = getLeft(whitened);
        byte[] right = getRight(whitened);

        int rounds = 4;
        byte[][] subKeys = genetateSubKeys(key, rounds);
        byte[] result = feistelRounds(left, right, subKeys);

        return result;
    }

    /**
     * Decrypt one block
     * @param block
     * @param key
     * @return
     */
    public byte[] decryptBlock(byte[] block, byte[] key)
    {
        byte[] left = getLeft(block);
        byte[] right = getRight(block);

        int rounds = 4;
        byte[][] subKeys = genetateSubKeys(key, rounds);
        byte[][] reversed = reverseSubKeys(subKeys);

        byte[] afterRounds = feistelRoundsDecrypt(left, right, reversed);

        byte[] original = xorBlock(afterRounds, key);

        return original;
    }

    /**
     * Split data to 16 bytes block.
     * Camellia works on 128 bit key, so the data need to be split to 16 bytes (128 bits) blocks.
     * The data must be padded.
     * @param data File Data
     * @return Array of blocks in 16 bytes
     */
    public static byte[][] splitToBlocks(byte[] data) 
    {
        int blockSize = 16;
        int numBlocks = data.length / blockSize;

        byte[][] blocks = new byte[numBlocks][blockSize];

        for (int i = 0; i < numBlocks; i++) // for each block
        {
            for (int j = 0; j < blockSize; j++) // insert 16 bytes in the block
            { 
                int index = i * blockSize + j;
                blocks[i][j] = data[index];
            }
        }

        return blocks;
    }

    /**
     * Join blocks of data
     * @param blocks
     * @return
     */
    public static byte[] joinBlocks(byte[][] blocks)
    {
        int blockSize = 16;
        byte[] data = new byte[blocks.length * blockSize];

        int index = 0;
        for (int i = 0; i < blocks.length; i++) 
        {
            for (int j = 0; j < blockSize; j++) 
            {
                data[index++] = blocks[i][j];
            }
        }

        return data;
    }

    /**
     * Padding function that using PKCS#7 method
     * @param data
     * @return data with padding
     */
    public static byte[] addPadding(byte[] data)
    {
        int blockSize = 16;
        int padLen = blockSize - (data.length % blockSize);

        if (padLen == 0)
            padLen = blockSize;

        byte[] padded = new byte[data.length + padLen];

        // Copy data to padded
        for (int i = 0; i < data.length; i++)
            padded[i] = data[i];

        for (int i = data.length; i < padded.length; i++)
            padded[i] = (byte) padLen;

        return padded;
    }

    /**
     * Remove padding from data using PKCS#7 method
     * @param data
     * @return unpadded data
     */
    public byte[] removePadding(byte[] data)
    {
        int padLed = data[data.length - 1] & 0xFF;

        byte[] unpadded = new byte[data.length - padLed];
        for (int i = 0; i < unpadded.length; i++)
            unpadded[i] = data[i];

        return unpadded;
    }

    /**
     * Return the left side of the block
     * @param block
     * @return
     */
    public static byte[] getLeft(byte[] block)
    {
        byte[] left = new byte[8];
        for (int i = 0; i < left.length; i++) 
        {
            left[i] = block[i];
        }

        return left;
    }

    /**
     * Return the right side of the block
     * @param block
     * @return
     */
    public static byte[] getRight(byte[] block)
    {
        byte[] right = new byte[8];
        for (int i = 0; i < right.length; i++) 
        {
            right[i] = block[i+8];
        }

        return right;
    }

    /**
     * Do XOR on a block
     * @param block block of 128 bit
     * @param key key of 128 bit
     * @return XOR on block
     */
    public static byte[] xorBlock(byte[] block, byte[] key)
    {
        byte[] result = new byte[block.length];

        for (int i = 0; i < block.length; i++) 
        {
            result[i] = (byte) (block[i] ^ key[i]); // XOR
        }

        return result;
    }

    /**
     * Rotate left n times with 8 bit number,
     * @param x
     * @param n
     * @return
     */
    public static int rotl8(int x, int n)
    {
        x &= 0xFF;
        return ((x << n) | (x >>> (8 - n))) & 0xFF;
    }

    private static int sbox1(int x)
    {
        return SBOX1[x & 0xFF] & 0xFF;
    }

    private static int sbox2(int x)
    {
        return rotl8(sbox1(x), 1);
    }

    private static int sbox3(int x)
    {
        return rotl8(sbox1(x), 7);
    }

    private static int sbox4(int x)
    {
        return sbox1(rotl8(x, 1));
    }
}
