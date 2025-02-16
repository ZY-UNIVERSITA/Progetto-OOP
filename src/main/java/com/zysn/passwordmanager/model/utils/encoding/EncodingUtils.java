package com.zysn.passwordmanager.model.utils.encoding;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Base64;

import org.apache.commons.codec.binary.Base32;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The EncodingUtils class provides utility methods for encoding and decoding
 * data.
 */
public class EncodingUtils {
    /**
     * Converts the specified char array to a byte array using UTF-8 encoding.
     *
     * @param source the char array to be converted
     * @return the byte array, or null if the conversion fails
     */
    public static byte[] charToByteConverter(final char[] source) {
        return charToByteConverter(source, "UTF-8");
    }

    /**
     * Converts the specified char array to a byte array using custom encoding.
     *
     * @param source      the char array to be converted
     * @param charsetName the name of the charset
     * @return the byte array, or null if the conversion fails
     */
    public static byte[] charToByteConverter(final char[] source, final String charsetName) {
        final Charset charset = Charset.forName(charsetName);
        final CharsetEncoder encoder = charset.newEncoder();

        final CharBuffer charBuffer = CharBuffer.wrap(source);

        byte[] byteArray = null;

        try {
            final ByteBuffer byteBuffer = encoder.encode(charBuffer);
            byteArray = new byte[byteBuffer.remaining()];
            byteBuffer.get(byteArray);

        } catch (final CharacterCodingException e) {
            System.err.println("Error trying to convert char[] into byte[].");
        }

        return byteArray;
    }

    /**
     * Converts a byte array to a character array using the default charset (UTF-8).
     *
     * @param source the byte array to be converted
     * @return the resulting character array
     */
    public static char[] byteToCharConverter(final byte[] source) {
        return byteToCharConverter(source, "UTF-8");
    }

    /**
     * Converts a byte array to a character array using the specified charset.
     *
     * @param source      the byte array to be converted
     * @param charsetName the name of the charset to be used for conversion
     * @return the resulting character array
     * @throws IllegalArgumentException if the specified charset is not supported
     */
    public static char[] byteToCharConverter(final byte[] source, final String charsetName) {
        final Charset charset = Charset.forName(charsetName);
        final CharsetDecoder decoder = charset.newDecoder();

        final ByteBuffer byteBuffer = ByteBuffer.wrap(source);

        char[] charArray = null;

        try {
            final CharBuffer charBuffer = decoder.decode(byteBuffer);
            charArray = new char[charBuffer.remaining()];
            charBuffer.get(charArray);

        } catch (final CharacterCodingException e) {
            System.err.println("Error trying to convert byte[] into char[].");
        }

        return charArray;
    }

    /**
     * Encodes the given byte array into a Base64 char array.
     *
     * @param source the byte array to be encoded
     * @return a char array representing the Base64-encoded string of the source
     *         byte array
     * @throws IllegalArgumentException if the source byte array is null
     */
    public static char[] byteToBase64(final byte[] source) {
        if (source == null) {
            throw new IllegalArgumentException("The source byte array cannot be null");
        }
        return Base64.getEncoder().encodeToString(source).toCharArray();
    }

    /**
     * Decodes the given Base64 char array into a byte array.
     *
     * @param source the char array representing the Base64-encoded string
     * @return a byte array decoded from the Base64 char array
     * @throws IllegalArgumentException if the source char array is null
     */
    public static byte[] base64ToByte(final char[] source) {
        if (source == null) {
            throw new IllegalArgumentException("The source char array cannot be null");
        }
        return Base64.getDecoder().decode(String.valueOf(source));
    }

    /**
     * Encodes the given byte array into a Base32 encoded byte array.
     *
     * @param source the byte array to be encoded
     * @return the Base32 encoded byte array
     */
    public static byte[] byteToBase32Byte(final byte[] source) {
        final Base32 base32 = new Base32();
        return base32.encode(source);
    }

    /**
     * Deserializes the given byte array to an instance of the specified class type.
     *
     * @param data               The byte array to deserialize.
     * @param classTypeReference The TypeReference of the class type to deserialize
     *                           to.
     * @param <T>                The type of the class.
     * @return The deserialized instance of the specified class type.
     */
    public static <T> T deserializeData(final byte[] data, final TypeReference<T> classTypeReference) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        T deserializedData = null;

        try {
            deserializedData = objectMapper.readValue(data, classTypeReference);
        } catch (final StreamReadException e) {
            System.err.println("Error during data deserialization: malformed input detected.");
        } catch (final DatabindException e) {
            System.err.println("Error during data deserialization: issue with data binding.");
        } catch (final IOException e) {
            System.err.println("Error during data deserialization: input/output operation issue.");
        }

        return deserializedData;
    }

    /**
     * Serializes the given data to a byte array.
     *
     * @param data The data to serialize.
     * @param <T>  The type of the data.
     * @return The serialized byte array.
     */
    public static <T> byte[] serializeData(final T data) {
        final ObjectMapper objectMapper = new ObjectMapper();

        byte[] serializedData = null;

        try {
            serializedData = objectMapper.writeValueAsBytes(data);
        } catch (final JsonProcessingException e) {
            System.err.println(
                    "Error during data serialization: ensure the data structure is compatible with JSON format.");
        }

        return serializedData;
    }
}
