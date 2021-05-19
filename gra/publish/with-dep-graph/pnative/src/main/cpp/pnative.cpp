#include "stdafx.h"

#if defined(__GNUC__)
    #define PNATIVE_EXPORT extern "C" __attribute__ ((visibility("default")))
#else
    #error Unsupported compiler
#endif

PNATIVE_EXPORT int
PNativeFactory()
{
    return 3;
}
