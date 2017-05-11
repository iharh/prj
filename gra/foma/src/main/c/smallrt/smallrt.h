#ifdef  __cplusplus
extern "C" {
#endif
#include <stdio.h>

#ifdef _MSC_VER
#define FEXPORT __declspec(dllexport)
#define INLINE
#else
#define INLINE inline
#define FEXPORT __attribute__((visibility("default")))
#endif

//#ifndef __cplusplus
//  #ifndef bool
//    #define bool int
//    #define false ((bool)0)
//    #define true  ((bool)1)
//  #endif
//#endif // __cplusplus

// _Bool changed to Boolean in HFST
//define Boolean bool

/* Misc */
FEXPORT char *xxstrndup(const char *s, size_t n);
FEXPORT char *xxstrdup(const char *s);
FEXPORT void *xxmalloc(size_t size);
FEXPORT void *xxcalloc(size_t nmemb, size_t size);
FEXPORT void *xxrealloc(void *ptr, size_t size);
FEXPORT void xxfree(void *ptr);

#ifdef  __cplusplus
}
#endif
