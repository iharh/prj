#ifdef  __cplusplus
extern "C" {
#endif
#include <stdio.h>
//include <inttypes.h>
#include <string.h>

#ifdef ORIGINAL
#define INLINE inline

#define FEXPORT __attribute__((visibility("default")))
#else // #ifdef ORIGINAL
  #ifdef _MSC_VER
    #define FEXPORT __declspec(dllexport)
    #define INLINE
  #else
    #define INLINE inline
    #define FEXPORT __attribute__((visibility("default")))
  #endif
#endif // #ifdef ORIGINAL

#ifndef __cplusplus
  #ifndef bool
    #define bool int
    #define false ((bool)0)
    #define true  ((bool)1)
  #endif
#endif // __cplusplus

// _Bool changed to Boolean in HFST
#define Boolean bool

/* Misc */
char *xxstrndup(const char *s, size_t n);
char *xxstrdup(const char *s);
FEXPORT void *xxmalloc(size_t size);
void *xxcalloc(size_t nmemb, size_t size);
void *xxrealloc(void *ptr, size_t size);
void xxfree(void *ptr);

#ifdef  __cplusplus
}
#endif
