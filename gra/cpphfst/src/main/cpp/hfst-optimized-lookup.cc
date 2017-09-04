/*
  
  Copyright 2009 University of Helsinki
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
  http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  
*/

/*
  NOTE:
  THIS SINGLE-FILE VERSION WAS PUT TOGETHER FROM A MULTI-FILE VERSION
  SO THE CURRENT STRUCTURE IS NOT SO GREAT. TODO: FIX THIS.
  TODO: USE THE EXISTING HFST-TOOLS FRAMEWORK BETTER.
 */

#include "hfst-optimized-lookup.h"

// the following flags are only meaningful with certain debugging #defines
bool timingFlag = false;
bool printDebuggingInformationFlag = false;

OutputType outputType = xerox;

bool verboseFlag = false;

bool displayWeightsFlag = false;
bool displayUniqueFlag = false;
bool echoInputsFlag = false;
bool beFast = false;
int maxAnalyses = INT_MAX;

void TransducerHeader::skip_hfst3_header(FILE * f)
{
    const char* header1 = "HFST";
    unsigned int header_loc = 0; // how much of the header has been found
    int c = 0;
    for(header_loc = 0; header_loc < strlen(header1) + 1; header_loc++)
    {
        c = getc(f);
        if(c != header1[header_loc]) {
            break;
        }
    }
    if(header_loc == strlen(header1) + 1) // we found it
    {
        unsigned short remaining_header_len;
        if (fread(&remaining_header_len,
                  sizeof(remaining_header_len), 1, f) != 1 ||
            getc(f) != '\0') {
            throw HeaderParsingException();
        }
        char * headervalue = new char[remaining_header_len];
        if (fread(headervalue, remaining_header_len, 1, f) != 1)
        {
            throw HeaderParsingException();
        }
        if (headervalue[remaining_header_len - 1] != '\0') {
            throw HeaderParsingException();
        }
        std::string header_tail(headervalue, remaining_header_len);
        size_t type_field = header_tail.find("type");
        if (type_field != std::string::npos) {
            if (header_tail.find("HFST_OL") != type_field + 5 &&
                header_tail.find("HFST_OLW") != type_field + 5) {
                delete [] headervalue; // TODO: was no [], but compiler complained
                throw HeaderParsingException();
            }
        }
        delete [] headervalue;
    } else // nope. put back what we've taken
    {
        ungetc(c, f); // first the non-matching character
            for(int i = header_loc - 1; i>=0; i--) {
// then the characters that did match (if any)
                ungetc(header1[i], f);
            }
    }
}

TransducerAlphabet::TransducerAlphabet(FILE * f,SymbolNumber symbol_number)
:
    number_of_symbols(symbol_number),
    kt(new KeyTable),
    line((char*)(malloc(1000)))
{
    feat_num = 0;
    for (SymbolNumber k = 0; k < number_of_symbols; ++k)
    {
        get_next_symbol(f, k);
    }
    // assume the first symbol is epsilon which we don't want to print
    put_sym(0, "");
}

TransducerAlphabet::~TransducerAlphabet()
{
    for (auto itr = kt->begin(); itr != kt->end(); ++itr)
    {
        char *p = const_cast<char *>(itr->second);
        free(p);
    }
    delete kt;
    free(line);
}

void
TransducerAlphabet::get_next_symbol(FILE *f, SymbolNumber k)
{
  int byte;
  char *sym = line;
  while ( (byte = fgetc(f)) != 0 )
    {
      if (byte == EOF)
        {
          std::cerr << "Could not parse transducer; wrong or corrupt file?" << std::endl;
          exit(1);
        }
      *sym = (char) byte;
      ++sym;
    }
  *sym = 0;
  if (strlen(line) >= 5 && line[0] == '@' && line[strlen(line) - 1] == '@' && line[2] == '.')
    { // a special symbol needs to be parsed
      std::string feat;
      char *c = line;
      // as long as we're working with utf-8, this should be ok
      for (c +=3; *c != '.' && *c != '@'; c++) { feat.append(c,1); }
      if (feature_bucket.count(feat) == 0)
        {
          feature_bucket[feat] = feat_num;
          ++feat_num;
        }
      put_sym(k, "");
      
#if OL_FULL_DEBUG
      std::cout << "symbol number " << k << " (flag) is \"" << line << "\"" << std::endl;
      put_sym(k, line);
#endif
      
      return;
    }

#if OL_FULL_DEBUG
  std::cout << "symbol number " << k << " is \"" << line << "\"" << std::endl;
#endif
  
  put_sym(k, line);
}


LetterTrie::LetterTrie()
:
    letters(UCHAR_MAX, (LetterTrie *) NULL),
    symbols(UCHAR_MAX,NO_SYMBOL_NUMBER)
{
}

LetterTrie::~LetterTrie()
{
    // TODO: overlap with KeyTable stuff ???
    for (auto itr = letters.begin(); itr != letters.end(); ++itr) {
        delete (*itr);
    }
}

void LetterTrie::add_string(const char * p, SymbolNumber symbol_key)
{
  if (p == NULL || strlen(p) == 0) {
      return;
  }
    // ==24623==ERROR: AddressSanitizer: global-buffer-overflow on address 0x003beb98eb41 at pc 0x003beb911668 bp 0x7ffc2c0a5d60 sp 0x7ffc2c0a5d58
    // READ of size 1 at 0x003beb98eb41 thread T0
  if (*(p+1) == 0)
    {
      symbols[(unsigned char)(*p)] = symbol_key;
      return;
    }
  if (letters[(unsigned char)(*p)] == NULL)
    {
      letters[(unsigned char)(*p)] = new LetterTrie();
    }
  letters[(unsigned char)(*p)]->add_string(p+1,symbol_key);
}

SymbolNumber LetterTrie::find_key(const char ** p)
{
  const char * old_p = *p;
  ++(*p);
  if (letters[(unsigned char)(*old_p)] == NULL)
    {
      return symbols[(unsigned char)(*old_p)];
    }
  SymbolNumber s = letters[(unsigned char)(*old_p)]->find_key(p);
  if (s == NO_SYMBOL_NUMBER)
    {
      --(*p);
      return symbols[(unsigned char)(*old_p)];
    }
  return s;
}

void
Encoder::read_input_symbols(KeyTable *kt)
{
    for (SymbolNumber k = 0; k < number_of_input_symbols; ++k)
    {
#if DEBUG
        assert(kt->find(k) != kt->end());
#endif
        const char *p = kt->operator[](k);
        if ((strlen(p) == 1) && (unsigned char)(*p) <= 127) {
            ascii_symbols[(unsigned char)(*p)] = k;
        }
        letters.add_string(p, k);
    }
}

SymbolNumber Encoder::find_key(const char ** p)
{
  if (ascii_symbols[(unsigned char)(**p)] == NO_SYMBOL_NUMBER)
    {
      return letters.find_key(p);
    }
  SymbolNumber s = ascii_symbols[(unsigned char)(**p)];
  ++(*p);
  return s;
}

template <class genericTransducer>
void runTransducer (genericTransducer T)
{
  SymbolNumber * input_string = (SymbolNumber *)(malloc(2000));
  for (int i = 0; i < 1000; ++i)
    {
      input_string[i] = NO_SYMBOL_NUMBER;
    }
  
  char * str = (char*)(malloc(MAX_IO_STRING * sizeof(char)));  
  *str = 0;
  char * old_str = str;

  while(std::cin.getline(str,MAX_IO_STRING))
    {
      // Carriage returns in Windows..
      unsigned int last_char_index = (unsigned int)(std::cin.gcount()) - 2;
      if (str[last_char_index] == '\r')
        str[last_char_index] = '\0';

      if (echoInputsFlag)
        {
          std::cout << str << std::endl;
        }
      int i = 0;
      SymbolNumber k = NO_SYMBOL_NUMBER;
      bool failed = false;
      for ( const char ** Str = const_cast<const char**>(&str); **Str != 0; )
        {
          k = T.find_next_key(Str);
#if OL_FULL_DEBUG
          std::cout << "INPUT STRING ENTRY " << i << " IS " << k << std::endl;
#endif
          if (k == NO_SYMBOL_NUMBER)
            {
              if (echoInputsFlag)
                {
                  std::cout << std::endl;
                }
              failed = true;
              break;
            }
          input_string[i] = k;
          ++i;
        }

      str = old_str;
      if (failed)
        { // tokenization failed
          if (outputType == xerox)
            {
              std::cout << str << "\t+?" << std::endl;
              std::cout << std::endl;
            }
          continue;
        }

      input_string[i] = NO_SYMBOL_NUMBER;

      T.analyze(input_string);
      T.printAnalyses(std::string(str));
    }
}

/**
 * BEGIN old transducer.cc
 */

/**
 * BEGIN old transducer-weighted.cc
 */

bool TransitionWIndex::matches(SymbolNumber s)
{
  
  if (input_symbol == NO_SYMBOL_NUMBER)
    {
      return false;
    }
  if (s == NO_SYMBOL_NUMBER)
    {
      return true;
    }
  return input_symbol == s;
}

bool TransitionW::matches(SymbolNumber s)
{
  
  if (input_symbol == NO_SYMBOL_NUMBER)
    {
      return false;
    }
  if (s == NO_SYMBOL_NUMBER)
    {
      return true;
    }
  return input_symbol == s;
}

void IndexTableReaderW::get_index_vector(void)
{
  for (size_t i = 0;
       i < number_of_table_entries;
       ++i)
    {
      size_t j = i * TransitionWIndex::SIZE;
      SymbolNumber * input = (SymbolNumber*)(TableIndices + j);
      TransitionTableIndex * index = 
        (TransitionTableIndex*)(TableIndices + j + sizeof(SymbolNumber));
      indices.push_back(new TransitionWIndex(*input,*index));
    }
}

void TransitionTableReaderW::Set(TransitionTableIndex pos)
{
  if (pos >= TRANSITION_TARGET_TABLE_START)
    {
      position = pos - TRANSITION_TARGET_TABLE_START;
    }
  else
    {
      position = pos;
    }
}

void TransitionTableReaderW::get_transition_vector(void)
{
  for (size_t i = 0; i < number_of_table_entries; ++i)
    {
      size_t j = i * TransitionW::SIZE;
      SymbolNumber * input = (SymbolNumber*)(TableTransitions + j);
      SymbolNumber * output = 
        (SymbolNumber*)(TableTransitions + j + sizeof(SymbolNumber));
      TransitionTableIndex * target = 
        (TransitionTableIndex*)(TableTransitions + j + 2 * sizeof(SymbolNumber));
      Weight * weight =
        (Weight*)(TableTransitions + j + 2 * sizeof(SymbolNumber) + sizeof(TransitionTableIndex));
      transitions.push_back(new TransitionW(*input,
                                            *output,
                                            *target,
                                            *weight));
      
    }
  transitions.push_back(new TransitionW());
  transitions.push_back(new TransitionW());
}

bool TransitionTableReaderW::Matches(SymbolNumber s)
{
  TransitionW * t = transitions[position];
  return t->matches(s);
}

bool TransitionTableReaderW::get_finality(TransitionTableIndex i)
{
  if (i >= TRANSITION_TARGET_TABLE_START) 
    {
      return transitions[i - TRANSITION_TARGET_TABLE_START]->final();
    }
  else
    {
      return transitions[i]->final();
    }
}


TransducerW::TransducerW(FILE *f, TransducerHeader h, std::shared_ptr<TransducerAlphabet> pA)
:
    header(h),
    pAlphabet(pA),
    keys(pA->get_key_table()),
    index_reader(f,header.index_table_size()),
    transition_reader(f,header.target_table_size()),
    encoder(keys,header.input_symbol_count()),
    display_map(),
    output_string((SymbolNumber *)(malloc(2000))),
    indices(index_reader()),
    transitions(transition_reader()),
    current_weight(0.0)
{
    for (int i = 0; i < 1000; ++i)
    {
        output_string[i] = NO_SYMBOL_NUMBER;
    }
    set_symbol_table();
}

TransducerW::~TransducerW()
{
    free(output_string);
}

void
TransducerW::set_symbol_table(void)
{
    for(KeyTable::iterator it = keys->begin(); it != keys->end(); ++it)
    {
        const char *key_name = it->second;
        symbol_table.push_back(key_name);
    }
}

void
TransducerW::try_epsilon_transitions(SymbolNumber *input_symbol,
      SymbolNumber *output_symbol,
      SymbolNumber *original_output_string,
      TransitionTableIndex i)
{
#if OL_FULL_DEBUG
  std::cerr << "try epsilon transitions " << i << " " << current_weight << std::endl;
#endif

  if (transitions.size() <= i) 
    {
      return;
    }

  while ((transitions[i] != NULL) && (transitions[i]->get_input() == 0))
    {
      *output_symbol = transitions[i]->get_output();
      current_weight += transitions[i]->get_weight();
      get_analyses(input_symbol,
                   output_symbol+1,
                   original_output_string,
                   transitions[i]->target());
      current_weight -= transitions[i]->get_weight();
      ++i;
    }
  *output_symbol = NO_SYMBOL_NUMBER;
}

void TransducerW::try_epsilon_indices(SymbolNumber * input_symbol,
                                      SymbolNumber * output_symbol,
                                      SymbolNumber * original_output_string,
                                      TransitionTableIndex i)
{
#if OL_FULL_DEBUG
  std::cerr << "try indices " << i << " " << current_weight << std::endl;
#endif
  if (indices[i]->get_input() == 0)
    {
      try_epsilon_transitions(input_symbol,
                              output_symbol,
                              original_output_string,
                              indices[i]->target() - 
                              TRANSITION_TARGET_TABLE_START);
    }
}

void TransducerW::find_transitions(SymbolNumber input,
                                   SymbolNumber * input_symbol,
                                   SymbolNumber * output_symbol,
                                   SymbolNumber * original_output_string,
                                   TransitionTableIndex i)
{
#if OL_FULL_DEBUG
  std::cerr << "find transitions " << i << " " << current_weight << std::endl;
#endif

  if (transitions.size() <= i) 
    {
      return;
    }
  while (transitions[i]->get_input() != NO_SYMBOL_NUMBER)
    {
      
      if (transitions[i]->get_input() == input)
        {
          current_weight += transitions[i]->get_weight();
          *output_symbol = transitions[i]->get_output();
          get_analyses(input_symbol,
                       output_symbol+1,
                       original_output_string,
                       transitions[i]->target());
          current_weight -= transitions[i]->get_weight();
        }
      else
        {
          return;
        }
      ++i;
    }
}

void TransducerW::find_index(SymbolNumber input,
                             SymbolNumber * input_symbol,
                             SymbolNumber * output_symbol,
                             SymbolNumber * original_output_string,
                             TransitionTableIndex i)
{
#if OL_FULL_DEBUG
  std::cerr << "find index " << i << " " << current_weight << std::endl;
#endif
  if (indices.size() <= i) 
    {
      return;
    }
  
  if (indices[i+input]->get_input() == input)
    {
      
      find_transitions(input,
                       input_symbol,
                       output_symbol,
                       original_output_string,
                       indices[i+input]->target() - 
                       TRANSITION_TARGET_TABLE_START);
    }
}

void TransducerW::note_analysis(SymbolNumber * whole_output_string)
{
  std::string str = "";
  for (SymbolNumber * num = whole_output_string;
       *num != NO_SYMBOL_NUMBER;
       ++num)
    {
      str.append(symbol_table[*num]);
    }
  display_map.insert(std::pair<Weight, std::string>(current_weight, str));
}

void TransducerW::printAnalyses(std::string prepend)
{
  if (outputType == xerox && display_map.size() == 0)
    {
      std::cout << prepend << "\t+?" << std::endl;
      std::cout << std::endl;
      return;
    }
  int i = 0;
  DisplayMultiMap::iterator it = display_map.begin();
  while ( (it != display_map.end()) && (i < maxAnalyses))
    {
      if (outputType == xerox)
        {
          std::cout << prepend << "\t";
        }
      std::cout << (*it).second;
      if (displayWeightsFlag)
        {
          std::cout << '\t' << (*it).first;
        }
      std::cout << std::endl;
      ++it;
      ++i;
    }
  display_map.clear();
  std::cout << std::endl;
}

void TransducerW::get_analyses(SymbolNumber * input_symbol,
                               SymbolNumber * output_symbol,
                               SymbolNumber * original_output_string,
                               TransitionTableIndex i)
{
#if OL_FULL_DEBUG
  std::cerr << "get analyses " << i << " " << current_weight << std::endl;
#endif
  if (i >= TRANSITION_TARGET_TABLE_START )
    {
      i -= TRANSITION_TARGET_TABLE_START;
      
      try_epsilon_transitions(input_symbol,
                              output_symbol,
                              original_output_string,
                              i+1);
      
      // input-string ended.
      if (*input_symbol == NO_SYMBOL_NUMBER)
        {
          *output_symbol = NO_SYMBOL_NUMBER;
          if (transitions.size() <= i) 
            {
              return;
            }
          if (final_transition(i))
            {
              current_weight += get_final_transition_weight(i);
              note_analysis(original_output_string);
              current_weight -= get_final_transition_weight(i);
            }
          return;
        }
      
      SymbolNumber input = *input_symbol;
      ++input_symbol;
      
      
      find_transitions(input,
                       input_symbol,
                       output_symbol,
                       original_output_string,
                       i+1);
    }
  else
    {
      
      try_epsilon_indices(input_symbol,
                          output_symbol,
                          original_output_string,
                          i+1);
      // input-string ended.
      if (*input_symbol == NO_SYMBOL_NUMBER)
        {
          *output_symbol = NO_SYMBOL_NUMBER;
          if (final_index(i))
            {
              current_weight += get_final_index_weight(i);
              note_analysis(original_output_string);
              current_weight -= get_final_index_weight(i);
            }
          return;
        }
      
      SymbolNumber input = *input_symbol;
      ++input_symbol;
      
      find_index(input,
                 input_symbol,
                 output_symbol,
                 original_output_string,
                 i+1);
    }
}
