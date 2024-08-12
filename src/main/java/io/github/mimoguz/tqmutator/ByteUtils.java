package io.github.mimoguz.tqmutator;

public final class ByteUtils {
    public static int indexOf(byte[] haystack, byte[] needle) {
        if (needle == null || needle.length == 0) {
            return 0;
        }
        if (haystack == null || haystack.length == 0) {
            return -1;
        }

        // Pre-construct failure array for needle pattern
        final var failure = new int[needle.length];
        failure[0] = -1;
        for (var j = 1; j < needle.length; j++) {
            var i = failure[j -1];
            while (needle[j] != needle[i + 1] && i >= 0) {
                i = failure[i];
            }
            failure[j] = needle[j] == needle[i + 1] ? i + 1 : -1;
        }

        // Find match
        var i = 0;
        var j = 0;
        while (i < haystack.length && j < needle.length) {
            if (haystack[i] == needle[j]) {
                i++;
                j++;
            } else if (j == 0) {
                i++;
            } else {
                j = failure[j -1 ] + 1;
            }
        }

        return j == needle.length ? i - needle.length : -1;
    }
}
