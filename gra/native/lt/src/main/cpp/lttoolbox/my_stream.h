#pragma once

#include <iostream>
#include <fstream>
#include <sstream>

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
    std::wstringstream contents; 
public:
    f_stream_wifstream(const char *file_name)
    {
        std::wifstream s(file_name, std::wifstream::binary);

        // std::locale loc("en_US.UTF8");
        std::locale loc(std::locale(std::locale::classic(), "", std::locale::ctype)); 
        s.imbue(loc);

        //s.seekg(0, s.end);
        //std::wifstream::pos_type file_size = s.tellg();
        //s.seekg(0, s.beg);
        //std::wcout << L"file_size: " << file_size << std::endl;

        contents << s.rdbuf();
    }
    bool eof() { return contents.eof(); }
    wchar_t getWC() {
        wchar_t result;
        // >>-operator can't be used - it eats spaces
        contents.get(result); //std::wcout << L": " << result << std::endl;
        return result;
    }
};

typedef f_stream_wifstream &astream_t;
