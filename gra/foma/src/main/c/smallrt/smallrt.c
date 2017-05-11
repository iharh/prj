#include "smallrt.h"
#include <stdlib.h>
//#include <string.h>

char *xxstrndup(const char *s, size_t n) {
    char *r = NULL;
    const char *p = s;
    while(*p++ && n--);
    n = p - s - 1;
    r = (char *) xxmalloc(n + 1);
    if(r != NULL) {
        memcpy(r, s, n);
        r[n] = 0;
    }
    return r;
}

INLINE void *xxmalloc(size_t size) {
    return(malloc(size));
}

INLINE void xxfree(void *ptr) {
    free(ptr);
}

void *xxrealloc(void *ptr, size_t size) {
    return(realloc(ptr, size));
}

INLINE void *xxcalloc(size_t nmemb, size_t size) {
    return(calloc(nmemb,size));
}

INLINE char *xxstrdup(const char *s) {
  //    return(strdup(s));
  size_t size = strlen(s) + 1;
  char *p = malloc(size);
  if (p) {
    memcpy(p, s, size);
  }
  return p;
}
