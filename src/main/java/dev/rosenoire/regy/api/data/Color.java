package dev.rosenoire.regy.api.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.joml.Vector3f;
import org.joml.Vector4f;

/// Effectively immutable alternative to [java.awt.Color] caching its RGBA and HSV channels on construction.
/// @implNote While this class is effectively immutable in that the user may not modify the value of the channels of the
/// colour, this class is not technically immutable in that it does cache some of its lesser used values after first
/// computation (i.e. [#isDark()]).
public final class Color {
    /// [Codec] encoding every channel of this [Color] allowing for higher precision.
    public static final Codec<Color> CHANNEL_CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Codec.INT.fieldOf("red").forGetter(color -> color.red),
                    Codec.INT.fieldOf("green").forGetter(color -> color.green),
                    Codec.INT.fieldOf("blue").forGetter(color -> color.blue),
                    Codec.INT.fieldOf("alpha").forGetter(color -> color.alpha),
                    Codec.FLOAT.fieldOf("hue").forGetter(color -> color.hue),
                    Codec.FLOAT.fieldOf("saturation").forGetter(color -> color.saturation),
                    Codec.FLOAT.fieldOf("value").forGetter(color -> color.value)
            )
            .apply(instance, Color::new)
    );

    /// [StreamCodec] encoding a [Color] into a [RegistryFriendlyByteBuf] by storing each channel into the byte byf for more precision.
    public static final StreamCodec<RegistryFriendlyByteBuf, Color> PACKET_CHANNEL_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, color -> color.red,
            ByteBufCodecs.INT, color -> color.green,
            ByteBufCodecs.INT, color -> color.blue,
            ByteBufCodecs.INT, color -> color.alpha,
            ByteBufCodecs.FLOAT, color -> color.hue,
            ByteBufCodecs.FLOAT, color -> color.saturation,
            ByteBufCodecs.FLOAT, color -> color.value,
            Color::new
    );

    /// Faster & lighter [Codec] compared to [#CHANNEL_CODEC].
    /// @implNote Instead of encoding each channel, this codec encodes the [#argb()] of this colour, and then reconstructs
    /// the colour using [#fromARGB(int)].
    public static final Codec<Color> CODEC = Codec.INT.xmap(Color::fromARGB, Color::argb);

    /// Faster & lighter [StreamCodec] compared to [#PACKET_CHANNEL_CODEC].
    /// @implNote Instead of encoding each channel, this codec encodes the [#argb()] of this colour, and then reconstructs
    /// the colour using [#fromARGB(int)].
    public static final StreamCodec<RegistryFriendlyByteBuf, Color> PACKET_CODEC = ByteBufCodecs.INT
            .map(Color::fromARGB, Color::argb)
            .mapStream(FriendlyByteBuf::new);

    public static final Color WHITE = Color.rgb(255, 255, 255, 255);
    public static final Color BLACK = Color.rgb(0, 0, 0, 255);

    /// Red channel of this colour. Ranges between \[0..255\].
    /// @implNote This channel is computed even if the constructor used to create this instance was not RGB based.
    public final int red;
    /// Green channel of this colour. Ranges between \[0..255\].
    /// @implNote This channel is computed even if the constructor used to create this instance was not RGB based.
    public final int green;
    /// Blue channel of this colour. Ranges between \[0..255\].
    /// @implNote This channel is computed even if the constructor used to create this instance was not RGB based.
    public final int blue;
    /// Transparency channel of this colour. Ranges between \[0..255\].
    public final int alpha;

    /// Hue of this colour. Ranges between \[0f..1f\] where `0f` is the left-hand side red on the hue curve and `1f` is the
    /// right-hand side red on the hue curve.
    /// @implNote This channel is computed even if the constructor used to create this instance was not HSL based.
    public final float hue;
    /// Saturation of this colour. Ranges between \[0f..1f\] where `0f` is grayscale and `1f` is fully saturated. You may
    /// see this value as the "intensity" of the colour.
    /// @implNote This channel is computed even if the constructor used to create this instance was not HSL based.
    public final float saturation;
    /// Value of this colour. Ranges between \[0f..1f\] where `0f` is black and `1f` is pure. You may see this value as how
    /// much black is inject into a pure colour composed of the hue and of the saturation.
    /// @implNote This channel is computed even if the constructor used to create this instance was not HSL based.
    public final float value;

    private Boolean isDark;

    /// Initializes a new colour using every channel.
    /// @param r Red channel of this colour. Ranges between \[0..255\].
    /// @param g Green channel of this colour. Ranges between \[0..255\].
    /// @param b Blue channel of this colour. Ranges between \[0..255\].
    /// @param a Transparency channel of this colour. Ranges between \[0..255\].
    /// @param h Hue channel of this colour. Ranges between \[0f..1f\].
    /// @param s Saturation channel of this colour. Ranges between \[0f..1f\].
    /// @param v Value channel of this colour. Ranges between \[0f..1f\].
    /// @apiNote Do not use this constructor to create your colours.
    private Color(int r, int g, int b, int a, float h, float s, float v) {
        red = r;
        green = g;
        blue = b;
        alpha = a;

        hue = h;
        saturation = h;
        value = v;
    }

    /// Initializes a new colour the RGBA channels.
    /// @param red Red channel of this colour. Ranges between \[0..255\].
    /// @param green Green channel of this colour. Ranges between \[0..255\].
    /// @param blue Blue channel of this colour. Ranges between \[0..255\].
    /// @param alpha Transparency channel of this colour. Ranges between \[0..255\].
    /// @apiNote Do not use this constructor to create your colours.
    private Color(int red, int green, int blue, int alpha) {
        this.red = Math.clamp(red, 0, 255);
        this.green = Math.clamp(green, 0, 255);
        this.blue = Math.clamp(blue, 0, 255);
        this.alpha = Math.clamp(alpha, 0, 255);

        float[] components = java.awt.Color.RGBtoHSB(red, green, blue, null);
        hue = Math.clamp(components[0], 0, 1);
        saturation = Math.clamp(components[1], 0, 1);
        value = Math.clamp(components[2], 0, 1);
    }

    /// Initializes a new colour using the HSVA channels.
    /// @param hue Hue channel of this colour. Ranges between \[0f..1f\].
    /// @param saturation Saturation channel of this colour. Ranges between \[0f..1f\].
    /// @param value Value channel of this colour. Ranges between \[0f..1f\].
    /// @param alpha Transparency channel of this colour. Ranges between \[0..255\].
    /// @apiNote Do not use this constructor to create your colours.
    private Color(float hue, float saturation, float value, int alpha) {
        this.hue = Math.clamp(hue, 0, 1);
        this.saturation = Math.clamp(saturation, 0, 1);
        this.value = Math.clamp(value, 0, 1);
        this.alpha = Math.clamp(alpha, 0, 255);

        int rgb = Mth.hsvToRgb(hue, saturation, value);
        this.red = Math.clamp(ARGB.red(rgb), 0, 255);
        this.green = Math.clamp(ARGB.green(rgb), 0, 255);
        this.blue = Math.clamp(ARGB.blue(rgb), 0, 255);
    }

    /// Creates and returns a new [Color] using the given HSVa channels with an `Integer` for the transparency channel.
    /// @param hue Hue of this colour. Ranges between \[0f..1f\] where `0f` is the left-hand side red on the hue curve and
    /// `1f` is the right-hand side red on the hue curve.
    /// @param saturation Saturation of this colour. Ranges between \[0f..1f\] where `0f` is grayscale and `1f` is fully
    /// saturated. You may see this value as the "intensity" of the colour.
    /// @param value Value of this colour. Ranges between \[0f..1f\] where `0f` is black and `1f` is pure. You may see this
    /// value as how much black is inject into a pure colour composed of the hue and of the saturation.
    /// @param alpha Transparency channel of this colour. Ranges between \[0..255\].
    /// @return the created [Color].
    public static Color hsv(float hue, float saturation, float value, int alpha) {
        return new Color(hue, saturation, value, alpha);
    }

    /// Creates and returns a new [Color] using the given HSVa channels with an `Float` for the transparency channel.
    /// @param hue Hue of this colour. Ranges between \[0f..1f\] where `0f` is the left-hand side red on the hue curve and
    /// `1f` is the right-hand side red on the hue curve.
    /// @param saturation Saturation of this colour. Ranges between \[0f..1f\] where `0f` is grayscale and `1f` is fully
    /// saturated. You may see this value as the "intensity" of the colour.
    /// @param value Value of this colour. Ranges between \[0f..1f\] where `0f` is black and `1f` is pure. You may see this
    /// value as how much black is inject into a pure colour composed of the hue and of the saturation.
    /// @param alpha Transparency channel of this colour. Ranges between \[0f..1f\].
    /// @return the created [Color].
    public static Color hsv(float hue, float saturation, float value, float alpha) {
        return hsv(hue, saturation, value, Math.round(alpha * 255f));
    }

    /// Creates and returns a new [Color] using the given HSV channels with a fully opaque transparency.
    /// @param hue Hue of this colour. Ranges between \[0f..1f\] where `0f` is the left-hand side red on the hue curve and
    /// `1f` is the right-hand side red on the hue curve.
    /// @param saturation Saturation of this colour. Ranges between \[0f..1f\] where `0f` is grayscale and `1f` is fully
    /// saturated. You may see this value as the "intensity" of the colour.
    /// @param value Value of this colour. Ranges between \[0f..1f\] where `0f` is black and `1f` is pure. You may see this
    /// value as how much black is inject into a pure colour composed of the hue and of the saturation.
    /// @return the created [Color].
    public static Color hsv(float hue, float saturation, float value) {
        return hsv(hue, saturation, value, 255);
    }

    /// Creates and returns a new [Color] using the given RGBa `Integer` channels.
    /// @param red Red channel of the colour. Ranges between \[0..255\].
    /// @param green Green channel of the colour. Ranges between \[0..255\].
    /// @param blue Blue channel of the colour. Ranges between \[0..255\].
    /// @param alpha Transparency channel of the colour. Ranges between \[0..255\].
    /// @implNote Channels provided are clamped between \[0..255\] before creating the colour so to avoid any calculation
    /// error.
    /// @return the created [Color].
    public static Color rgb(int red, int green, int blue, int alpha) {
        return new Color(red, green, blue, alpha);
    }

    /// Creates and returns a new [Color] using the given RGBa `Float` channels.
    /// @param red Red channel of the colour. Ranges between \[0f..1f\].
    /// @param green Green channel of the colour. Ranges between \[0f..1f\].
    /// @param blue Blue channel of the colour. Ranges between \[0f..1f\].
    /// @param alpha Transparency channel of the colour. Ranges between \[0f..1f\].
    /// @implNote [#rgb(int, int, int, int)] is used to create the returned [Color] from the given channel values. The
    /// channel values are turned into `Integer` by using [Math#round(float)] after multiplying them by `255`.
    /// @return the created [Color].
    public static Color rgb(float red, float green, float blue, float alpha) {
        return rgb(
                Math.round(red * 255f),
                Math.round(green * 255f),
                Math.round(blue * 255f),
                Math.round(alpha * 255f)
        );
    }

    /// Creates and returns a new [Color] using the given RGB `Integer` channels with an opaque transparency.
    /// @param red Red channel of the colour. Ranges between \[0..255\].
    /// @param green Green channel of the colour. Ranges between \[0..255\].
    /// @param blue Blue channel of the colour. Ranges between \[0..255\]..
    /// @implNote Channels provided are clamped between \[0..255\] before creating the colour so to avoid any calculation
    /// error.
    /// @return the created [Color].
    public static Color rgb(int red, int green, int blue) {
        return new Color(red, green, blue, 255);
    }

    /// Creates and returns a new [Color] using the given RGB `Float` channels with an opaque transparency.
    /// @param red Red channel of the colour. Ranges between \[0f..1f\].
    /// @param green Green channel of the colour. Ranges between \[0f..1f\].
    /// @param blue Blue channel of the colour. Ranges between \[0f..1f\].
    /// @implNote [#rgb(int, int, int)] is used to create the returned [Color] from the given channel values. The
    /// channel values are turned into `Integer` by using [Math#round(float)] after multiplying them by `255`.
    /// @return the created [Color].
    public static Color rgb(float red, float green, float blue) {
        return rgb(Math.round(red * 255f), Math.round(green * 255f), Math.round(blue * 255f));
    }

    /// Creates and returns a new [Color] using the colour described by the given ARGB `Integer`.
    /// @param argb `Integer` describing the colour. Format is: `AARRGGBB`.
    /// @return created [Color].
    public static Color fromARGB(int argb) {
        return new Color(ARGB.red(argb), ARGB.green(argb), ARGB.blue(argb), ARGB.alpha(argb));
    }

    /// Converts a hexadecimal colour `String` into a [Color] and returns it.
    /// @param representation `String` containing the hexadecimal value.
    /// @implNote
    /// - The representation `String` may start with a hashtag `#` character, which will be stripped at the beginning of the
    /// conversion process.
    /// - The format of the hexadecimal `String` must follow the following: `AARRGGBB`, where the bytes representing the
    /// alpha channel are at **the start** of the hex.
    /// - If the provided representation does not match the required length of 8 characters (2 bytes per channel, 4 channels),
    /// a fully opaque transparency channel will first be added at the start of the representation. Then, the last hexadecimal
    /// value of the representation will be added `8 - representation.length` times at the end of the `String`. For instance,
    /// in the given representation `#FF5`, the representation will first be converted to `#FFFF5`, and then to `#FFFF5555`,
    /// creating a red colour.
    ///
    /// @see #argbHexadecimalOrFallback(String, Color)
    public static Color argbHexadecimalOrThrow(String representation) {
        // Making sure we don't have any whitespace getting in the way of the formatting.
        representation = representation.strip();
        // Removing # sign at the start of representation code.
        if (representation.startsWith("#")) representation = representation.substring(1);
        // Adding the alpha to the string if it's missing.
        if (representation.length() < 8) representation = "FF" + representation;

        StringBuilder representationBuilder = new StringBuilder(representation);
        while (representationBuilder.length() < 8) {
            representationBuilder.append(representationBuilder.charAt(representationBuilder.length() - 1));
        }

        representation = representationBuilder.toString();

        int alpha = Integer.valueOf(representation.substring(0, 2), 16);
        int red = Integer.valueOf(representation.substring(2, 4), 16);
        int green = Integer.valueOf(representation.substring(4, 6), 16);
        int blue = Integer.valueOf(representation.substring(6, 8), 16);

        return rgb(red, green, blue, alpha);
    }

    /// Converts a hexadecimal colour `String` into a [Color] and returns it.
    /// @param representation `String` containing the hexadecimal value.
    /// @param fallback [Color] to be returned in case the given representation is invalid.
    /// @implNote
    /// - The representation `String` may start with a hashtag `#` character, which will be stripped at the beginning of the
    /// conversion process.
    /// - The format of the hexadecimal `String` must follow the following: `AARRGGBB`, where the bytes representing the
    /// alpha channel are at **the start** of the hex.
    /// - If the provided representation does not match the required length of 8 characters (2 bytes per channel, 4 channels),
    /// a fully opaque transparency channel will first be added at the start of the representation. Then, the last hexadecimal
    /// value of the representation will be added `8 - representation.length` times at the end of the `String`. For instance,
    /// in the given representation `#FF5`, the representation will first be converted to `#FFFF5`, and then to `#FFFF5555`,
    /// creating a red colour.
    /// - If an exception is thrown during the creation of the colour, it will be ignored and `fallback` will be returned
    /// instead.
    ///
    /// @see #argbHexadecimalOrThrow(String)
    public static Color argbHexadecimalOrFallback(String representation, Color fallback) {
        try {
            return argbHexadecimalOrThrow(representation);
        }
        catch (Exception ignoredException) {
            return fallback;
        }
    }

    /// Whether the given `String` aRGB hexadecimal representation of a colour is valid or not.
    /// @param representation `String` representing the colour.
    /// @implNote
    /// - The representation `String` may start with a hashtag `#` character, which will be stripped at the beginning of the
    /// conversion process.
    /// - The format of the hexadecimal `String` must follow the following: `AARRGGBB`, where the bytes representing the
    /// alpha channel are at **the start** of the hex.
    /// - If the provided representation does not match the required length of 8 characters (2 bytes per channel, 4 channels),
    /// a fully opaque transparency channel will first be added at the start of the representation. Then, the last hexadecimal
    /// value of the representation will be added `8 - representation.length` times at the end of the `String`. For instance,
    /// in the given representation `#FF5`, the representation will first be converted to `#FFFF5`, and then to `#FFFF5555`,
    /// creating a red colour.
    /// @return If an exception is thrown during the creation of the colour, it will be ignored and `False` will be returned.
    /// Otherwise, `True` will be returned.
    ///
    /// @see #argbHexadecimalOrThrow(String)
    /// @see #argbHexadecimalOrFallback(String, Color)
    public static boolean validateArgbHexadecimal(String representation) {
        try {
            argbHexadecimalOrThrow(representation);
            return true;
        }
        catch (Exception ignoredException) {
            return false;
        }
    }

    /// Constructs a `String` representation of the hexadecimal aRGB representing this [Color].
    /// @param insertHashtag whether to insert a hashtag `#` character at the beginning of the returned `String`.
    /// @return the created `String` representing this colour.
    public String argbHexadecimal(boolean insertHashtag) {
        StringBuilder builder = new StringBuilder(Integer.toHexString(this.argb()));

        while (builder.length() < 8) {
            builder.insert(0, "FF");
        }

        if (insertHashtag) {
            builder.insert(0, "#");
        }

        return builder.toString();
    }

    /// Constructs a `String` representation of the hexadecimal RGB representing this [Color].
    /// @param insertHashtag whether to insert a hashtag `#` character at the beginning of the returned `String`.
    /// @return the created `String` representing this colour.
    public String rgbHexadecimal(boolean insertHashtag) {
        StringBuilder builder = new StringBuilder(Integer.toHexString(rgb() & 0xffffff));

        while (builder.length() < 6) {
            builder.insert(0, "0");
        }

        if (insertHashtag) {
            builder.insert(0, "#");
        }

        return builder.toString();
    }

    /// `Integer` representation of this colour using the format: `AARRGGBB`.
    public int argb() {
        return ARGB.color(alpha, red, green, blue);
    }

    /// `Integer` representation of this colour using the format: `RRGGBB`, omitting the alpha channel.
    public int rgb() {
        return ARGB.color(255, red, green, blue);
    }

    /// [Vector4f] representation of this colour, especially used for rendering purposes.
    public Vector4f rgbaVector() {
        return ARGB.vector4fFromARGB32(argb());
    }

    /// [Vector3f] representation of this colour, especially used for rendering purposes.
    public Vector3f rgbVector() {
        return ARGB.vector3fFromRGB24(rgb());
    }

    /// Detects whether a colour is to be considered "dark" or "light".
    /// Based on [this algorithm by Darel Rex Finley](https://alienryderflex.com/hsp.html).
    public boolean isDark() {
        if (isDark == null) {
            isDark = Math.sqrt(0.299 * (red * red) + 0.587 * (green * green) + 0.114 * (blue * blue)) <= 127.5;
        }

        return isDark;
    }

    /// Returns a new [Color] with its [#red] channel modified to be the given `Integer`.
    /// @param red the new red channel.
    /// @return created instance with the modified channel.
    public Color withRed(int red) {
        return rgb(red, green, blue, alpha);
    }

    /// Returns a new [Color] with its [#green] channel modified to be the given `Integer`.
    /// @param green the new green channel.
    /// @return created instance with the modified channel.
    public Color withGreen(int green) {
        return rgb(red, green, blue, alpha);
    }

    /// Returns a new [Color] with its [#blue] channel modified to be the given `Integer`.
    /// @param blue the new blue channel.
    /// @return created instance with the modified channel.
    public Color withBlue(int blue) {
        return rgb(red, green, blue, alpha);
    }

    /// Returns a new [Color] with its [#alpha] channel modified to be the given `Integer`.
    /// @param alpha the new transparency channel.
    /// @return created instance with the modified channel.
    public Color withAlpha(int alpha) {
        return rgb(red, green, blue, alpha);
    }

    /// Returns a new [Color] with its [#red] channel modified to be the given `Float`.
    /// @param red the new red channel.
    /// @return created instance with the modified channel.
    public Color withRed(float red) {
        return rgb(Math.round(red * 255f), green, blue, alpha);
    }

    /// Returns a new [Color] with its [#green] channel modified to be the given `Float`.
    /// @param green the new green channel.
    /// @return created instance with the modified channel.
    public Color withGreen(float green) {
        return rgb(red, Math.round(green * 255f), blue, alpha);
    }

    /// Returns a new [Color] with its [#blue] channel modified to be the given `Float`.
    /// @param blue the new blue channel.
    /// @return created instance with the modified channel.
    public Color withBlue(float blue) {
        return rgb(red, green, Math.round(blue * 255f), alpha);
    }

    /// Returns a new [Color] with its [#alpha] channel modified to be the given `Float`.
    /// @param alpha the new transparency channel.
    /// @return created instance with the modified channel.
    public Color withAlpha(float alpha) {
        return rgb(red, green, blue, Math.round(alpha * 255f));
    }

    /// Returns a new [Color] with its [#red] channel modified to be the [#red] of the `source` colour.
    /// @param source the colour to copy the channel from.
    /// @return created instance with the modified channel.
    public Color withRed(Color source) {
        return withRed(source.red);
    }

    /// Returns a new [Color] with its [#red] channel modified to be the [#red] of the `source` colour.
    /// @param source the colour to copy the channel from.
    /// @return created instance with the modified channel.
    public Color withGreen(Color source) {
        return withGreen(source.green);
    }

    /// Returns a new [Color] with its [#blue] channel modified to be the [#blue] of the `source` colour.
    /// @param source the colour to copy the channel from.
    /// @return created instance with the modified channel.
    public Color withBlue(Color source) {
        return withBlue(source.blue);
    }

    /// Returns a new [Color] with its [#alpha] channel modified to be the [#alpha] of the `source` colour.
    /// @param source the colour to copy the channel from.
    /// @return created instance with the modified channel.
    public Color withAlpha(Color source) {
        return withAlpha(source.alpha);
    }

    /// Returns a new [Color] with its [#hue] channel modified to be the given `Float`.
    /// @param hue the new hue channel.
    /// @return created instance with the modified channel.
    public Color withHue(float hue) {
        return hsv(hue, saturation, value, alpha);
    }

    /// Returns a new [Color] with its [#saturation] channel modified to be the given `Float`.
    /// @param saturation the new saturation channel.
    /// @return created instance with the modified channel.
    public Color withSaturation(float saturation) {
        return hsv(hue, saturation, value, alpha);
    }

    /// Returns a new [Color] with its [#value] channel modified to be the given `Float`.
    /// @param value the new value channel.
    /// @return created instance with the modified channel.
    public Color withValue(float value) {
        return hsv(hue, saturation, value, alpha);
    }

    /// Returns a new [Color] with its [#hue] channel modified to be the [#hue] of the `source` colour.
    /// @param source the colour to copy the channel from.
    /// @return created instance with the modified channel.
    public Color withHue(Color source) {
        return withHue(source.hue);
    }

    /// Returns a new [Color] with its [#saturation] channel modified to be the [#saturation] of the `source` colour.
    /// @param source the colour to copy the channel from.
    /// @return created instance with the modified channel.
    public Color withSaturation(Color source) {
        return withSaturation(source.saturation);
    }

    /// Returns a new [Color] with its [#value] channel modified to be the [#value] of the `source` colour.
    /// @param source the colour to copy the channel from.
    /// @return created instance with the modified channel.
    public Color withValue(Color source) {
        return withValue(source.value);
    }

    /// Linearly interpolates between this [Color] and `end` by the Δ `Float` in the `sRGB` space.
    /// @param end the target colour to reach when Δ is `1f`.
    /// @param delta the delta, or "time" of the linear interpolation. A value of `0f` means the return value will be equal
    /// to this colour, and a value of `1f` means the return value will be equal to the `end` colour.
    /// @implNote the given Δ is clamped between \[0f..1f\] before being lerped.
    /// @return the lerped colour.
    public Color lerpSRGB(Color end, float delta) {
        return Color.fromARGB(ARGB.srgbLerp(delta, argb(), end.argb()));
    }

    /// Linearly interpolates between this [Color] and `end` by the Δ `Float` in the linear space, yielding generally
    /// visually more satisfying results.
    /// @param end the target colour to reach when Δ is `1f`.
    /// @param delta the delta, or "time" of the linear interpolation. A value of `0f` means the return value will be equal
    /// to this colour, and a value of `1f` means the return value will be equal to the `end` colour.
    /// @implNote the given Δ is clamped between \[0f..1f\] before being lerped.
    /// @return the lerped colour.
    public Color lerpLinear(Color end, float delta) {
        return Color.fromARGB(ARGB.linearLerp(delta, argb(), end.argb()));
    }
}
