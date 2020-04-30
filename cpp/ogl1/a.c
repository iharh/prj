#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#include <GL/gl.h>
//#include <GL/glu.h>
#include <GL/glut.h>

void drawCircle(float x, float y, float r, int amountSegments) { 
    float tmpI;

    // Drawing is done by specifying a sequence of vertices.  The way these
    // vertices are connected (or not connected) depends on the argument to
    // glBegin.  GL_POLYGON constructs a filled polygon.
    glBegin(GL_LINE_LOOP); // GL_POLYGON
        for (int i = 0; i < amountSegments; ++i) {
            tmpI = (float)i;
            float angle = tmpI * 2.0 * 3.1415926 / (float)amountSegments;
            float dx = r * cosf(angle);
            float dy = r * sinf(angle);
            glVertex2f(x + dx, y + dy);
        }

        // glVertex3f(-0.6, -0.75, 0); // 0.5
        // glVertex3f(0.6, -0.75, 0);
        // glVertex3f(0, 0.75, 0);

        /*
        glVertex3f(-0.5, -0.5, 0);
        glVertex3f(-0.5,  0.5, 0);
        glVertex3f( 0.5,  0.5, 0);
        glVertex3f( 0.5, -0.5, 0);
        */
    glEnd();
}

// Clears the current window and draws a triangle.
void display() {
    // Set every pixel in the frame buffer to the current clear color.
    glClear(GL_COLOR_BUFFER_BIT);

    glColor3f(1.0f, 0.0f, 0.0f);
    drawCircle(0.0f, 0.0f, 1.0f, 8);

    // Flush drawing command buffer to make drawing happen as soon as possible.
    glFlush();
}

// Initializes GLUT, the display mode, and main window; registers callbacks;
// enters the main event loop.
int main(int argc, char** argv) {

  // Use a single buffered window in RGB mode (as opposed to a double-buffered
  // window or color-index mode).
  glutInit(&argc, argv);
  glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);

  // Position window at (80,80)-(480,380) and give it a title.
  glutInitWindowPosition(80, 80);
  glutInitWindowSize(400, 300);
  glutCreateWindow("A Simple Triangle - Artem!");

  // Tell GLUT that whenever the main window needs to be repainted that it
  // should call the function display().
  glutDisplayFunc(display);

  // Tell GLUT to start reading and processing events.  This function
  // never returns; the program only exits when the user closes the main
  // window or kills the process.
  glutMainLoop();
}
