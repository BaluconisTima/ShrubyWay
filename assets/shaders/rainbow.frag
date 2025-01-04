#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;

uniform sampler2D u_texture;
uniform float u_time;

void main() {
    vec4 color = texture2D(u_texture, v_texCoord);

    if (color.r == 0.0 && color.g == 0.0 && color.b == 0.0) {
        gl_FragColor = color;
        return;
    }

    float wave = sin(u_time + v_texCoord.x * 10.0) * 0.5 + 0.5;
    float r = wave * color.r;
    float g = wave * color.g;
    float b = wave * color.b;

    gl_FragColor = vec4(r, g, b, color.a);
}
