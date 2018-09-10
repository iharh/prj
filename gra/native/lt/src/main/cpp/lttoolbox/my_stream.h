#pragma once

#include <iostream>
#include <fstream>

class f_stream_stdio {
private:
    FILE *pF;
public:
    f_stream_stdio(const char *file_name, const char *mode) { pF = fopen(file_name, mode); }
    ~f_stream_stdio() { fclose(pF); }
    bool eof() { return feof(pF); }
    wchar_t getWC() {
        wchar_t result = static_cast<wchar_t>(fgetwc_unlocked(pF));
        //std::wcout << L": " << result << std::endl;
        return result;
    }
};

class f_stream_wifstream {
private:
    std::wifstream s;
public:
    f_stream_wifstream(const char *file_name)
    :
        s(file_name, std::wifstream::binary)
    {
        std::locale loc(std::locale(std::locale::classic(), "", std::locale::ctype)); // std::locale loc("en_US.UTF8");
        s.imbue(loc);
    }
    bool eof() { return s.eof(); }
    wchar_t getWC() {
        wchar_t result;
        // >>-operator can't be used - it eats spaces
        s.get(result); 
        //std::wcout << L": " << result << std::endl;
        return result;
    }
};

typedef f_stream_wifstream &astream_t;
