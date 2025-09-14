package com.shrubyway.game.noise;
import java.util.Random;

public class NoiseGenerator {

    public static float[][] generate_static_noise(int width, int height, int seed) {
        float[][] noise = new float[width][height];
        Random rand = new Random(seed);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                noise[x][y] = rand.nextFloat();
            }
        }
        return noise;
    }

    private static float interpolate(float a, float b, float x) {
        float ft = x * (float)Math.PI;
        float f = (1 - (float)Math.cos(ft)) * 0.5f;
        return a * (1 - f) + b * f;
    }

    public static float[][] generate_smooth_noise(float[][] baseNoise, int octave) {
        int width = baseNoise.length;
        int height = baseNoise[0].length;
        float[][] smoothNoise = new float[width][height];

        int samplePeriod = 1 << octave;
        float sampleFrequency = 1.0f / samplePeriod;

        for (int x = 0; x < width; x++) {
            int sample_x0 = (x / samplePeriod) * samplePeriod;
            int sample_x1 = (sample_x0 + samplePeriod) % width;
            float horizontal_blend = (x - sample_x0) * sampleFrequency;

            for (int y = 0; y < height; y++) {
                int sample_y0 = (y / samplePeriod) * samplePeriod;
                int sample_y1 = (sample_y0 + samplePeriod) % height;
                float vertical_blend = (y - sample_y0) * sampleFrequency;

                float top = interpolate(baseNoise[sample_x0][sample_y0], baseNoise[sample_x1][sample_y0], horizontal_blend);
                float bottom = interpolate(baseNoise[sample_x0][sample_y1], baseNoise[sample_x1][sample_y1], horizontal_blend);
                smoothNoise[x][y] = interpolate(top, bottom, vertical_blend);
            }
        }
        return smoothNoise;
    }

    public static float[][] generate_perlin_noise(int width, int height, int seed, int octaves, float persistence) {
        float[][] baseNoise = generate_static_noise(width, height, seed);
        float[][][] smoothNoise = new float[octaves][][];
        for (int i = 0; i < octaves; i++) {
            smoothNoise[i] = generate_smooth_noise(baseNoise, i);
        }
        float[][] perlinNoise = new float[width][height];
        float amplitude = 1.0f;
        float totalAmplitude = 0.0f;
        for (int octave = octaves - 1; octave >= 0; octave--) {
            amplitude *= persistence;
            totalAmplitude += amplitude;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    perlinNoise[x][y] += smoothNoise[octave][x][y] * amplitude;
                }
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                perlinNoise[x][y] /= totalAmplitude;
            }
        }
        return perlinNoise;
    }
}
