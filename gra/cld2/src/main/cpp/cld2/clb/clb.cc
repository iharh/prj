#if defined(_MSC_VER)
    #define OVERRIDE override
    #define SEALED sealed
    #define ABSTRACT abstract

    #define CLD_EXPORT extern "C" __declspec(dllexport)

    // We'd like to use override, sealed and abstract which ARE standard now
    #pragma warning(disable:4481)

#elif defined(__GNUC__)
    #define OVERRIDE
    #define SEALED
    #define ABSTRACT

    #define CLD_EXPORT extern "C" __attribute__ ((visibility("default")))

#else
    #error Unsupported compiler
#endif

CLD_EXPORT
int puts(char *in_str) {
    return 2;
}
