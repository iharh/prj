#include <iostream>

static std::string parseQuotedString(int &nStart, const std::string &strText)
{
    std::string strRes;
    if (strText[nStart] != '\"')
    {
        std::cout << "Expected starting '\"' symbol" << std::endl;
        return "";
    }

    ++nStart;
    std::cout << "nStart1: " << nStart  << std::endl;
    while (nStart < strText.length() && strText[nStart] != '\"')
    {
        if ('\\' == strText[nStart])
        {
            ++nStart;
            std::cout << "nStart2: " << nStart  << std::endl;
            switch (strText[nStart])
            {
            case '\\':
                strRes += strText[nStart];
                std::cout << "escaped back-slash" << std::endl;
                break;
            case '"':
                strRes += strText[nStart];
                break;
            case 'n':
                strRes += '\n';
                break;
            case 'r':
                strRes += '\r';
                break;
            case 't':
                strRes += '\t';
                break;
            default:
                std::cout << "Unknown escape sequence" << std::endl;
                return "";
            }
        }
        else
        {
            strRes += strText[nStart];
        }
        ++nStart;
        std::cout << "nStart3: " << nStart  << std::endl;
    }
    if (nStart == strText.length())
    {
        std::cout << "nStart4: " << nStart  << std::endl;
        std::cout << "Expected trailing '\"' symbol" << std::endl;
        return "";
    }
    ++nStart;
    return strRes;
}

int
main(void)
{
  std::cout << "start" << std::endl;
  std::string s1("\"//O-O\\\\\\\\\"");
  std::cout << "s1: " << s1 << std::endl;
  int nStart = 0;
  std::string s2 = parseQuotedString(nStart, s1);
  std::cout << "s2: " << s2 << std::endl;
  std::cout << "end" << std::endl;

  return 0;
}
