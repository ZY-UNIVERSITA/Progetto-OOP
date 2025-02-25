package com.zysn.passwordmanager.model.utils.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.zysn.passwordmanager.model.enums.CryptoLength;
import com.zysn.passwordmanager.model.utils.security.api.MustBeDestroyed;

/**
 * Utility class for cryptographic operations, including generating salts,
 * secure random passwords, and clearing memory.
 *
 * <p>
 * This class provides static methods to generate salts, generate secure
 * random passwords, and clear memory by overwriting char and byte arrays.
 * The methods in this class are designed to be used for cryptographic
 * purposes where secure random values and secure clearing of sensitive
 * data are required.
 * </p>
 */
public class CryptoUtils {
    /**
     * Generates a random salt of the specified length.
     *
     * @param length the length of the salt
     * @return a byte array containing the generated salt
     * @throws IllegalArgumentException if the length is 0 or less
     */
    public static byte[] generateSalt(final int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("The length of the salt cannot be 0 or less.");
        }

        final byte[] salt = new byte[length];

        try {
            final SecureRandom random = SecureRandom.getInstanceStrong();
            random.nextBytes(salt);
        } catch (final NoSuchAlgorithmException e) {
            System.err.println("Errore nella generazione del satl.");
        }

        return salt;
    }

    /**
     * Clears the contents of the specified char array by setting each element to
     * zero.
     *
     * @param source the char array to be cleared
     * @throws IllegalArgumentException if the source array is null
     */
    public static void cleanMemory(final char[] source) {
        if (source == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }

        Arrays.fill(source, '\u0000');
    }

    /**
     * Clears the contents of the specified byte array by setting each element to
     * zero.
     *
     * @param source the byte array to be cleared
     * @throws IllegalArgumentException if the source array is null
     */
    public static void cleanMemory(final byte[] source) {
        if (source == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }

        Arrays.fill(source, (byte) 0);
    }

    /**
     * Destroys the object provided by the getter function if it is not null.
     * 
     * @param <T>            the type of the object to be destroyed
     * @param getterFunction a Supplier that provides the object to be destroyed
     * @param setterFunction a Consumer that sets the object to null after it has
     *                       been destroyed
     */
    public static <T> void destroy(final Supplier<T> getterFunction, final Consumer<T> setterFunction) {
        Optional.ofNullable(getterFunction.get()).ifPresent(value -> {
            if (value instanceof byte[]) {
                CryptoUtils.cleanMemory((byte[]) value);
            } else if (value instanceof char[]) {
                CryptoUtils.cleanMemory((char[]) value);
            } else if (value instanceof MustBeDestroyed){
                final MustBeDestroyed mustBeDestroyed = (MustBeDestroyed) value;
                mustBeDestroyed.destroy();
            }
            
            setterFunction.accept(null);
        });
    }

    private CryptoUtils() {

    }

}
