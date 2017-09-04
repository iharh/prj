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

#pragma once

#include <cstring>
#include <memory>

#if defined(WIN32) || defined(WIN64)
#   define strdup _strdup
#endif

/*
config.h-defined constants
 */

static const char * PACKAGE_NAME = "hfst-optimized-lookup";
static const char * PACKAGE_BUGREPORT = "hfst-bugs@helsinki.fi";
static const char * PACKAGE_STRING = "hfst-optimized-lookup 1.2";

/*
  NOTE:
  THIS SINGLE-FILE VERSION WAS PUT TOGETHER FROM A MULTI-FILE VERSION
  SO THE CURRENT STRUCTURE IS NOT SO GREAT. TODO: FIX THIS.
 */

#include <cstdio>
#include <vector>
#include <map>
#include <set>
#include <cstdlib>
#include <climits>
#include <string>
#include <cassert>
#include <ctime>
#include <iostream>


enum OutputType {HFST, xerox};

#define MAX_IO_STRING 5000

typedef unsigned short SymbolNumber;
typedef unsigned int TransitionTableIndex;
typedef unsigned int TransitionNumber;
typedef unsigned int StateIdNumber;
typedef std::vector<SymbolNumber> SymbolNumberVector;
typedef std::map<SymbolNumber,const char *> KeyTable;
 
const StateIdNumber NO_ID_NUMBER = UINT_MAX;
const SymbolNumber NO_SYMBOL_NUMBER = USHRT_MAX;
const TransitionTableIndex NO_TABLE_INDEX = UINT_MAX;

enum HeaderFlag {Weighted, Deterministic, Input_deterministic, Minimized,
         Cyclic, Has_epsilon_epsilon_transitions,
         Has_input_epsilon_transitions, Has_input_epsilon_cycles,
         Has_unweighted_input_epsilon_cycles};

// This is 2^31, hopefully equal to UINT_MAX/2 rounded up.
// For some profound reason it can't be replaced with (UINT_MAX+1)/2.
const TransitionTableIndex TRANSITION_TARGET_TABLE_START = 2147483648u;

class HeaderParsingException: public std::exception
{
public:
    virtual const char* what() const throw()
    { return("Parsing error while reading header"); }
};

class TransducerHeader
{
 private:
  SymbolNumber number_of_symbols;
  SymbolNumber number_of_input_symbols;
  TransitionTableIndex size_of_transition_index_table;
  TransitionTableIndex size_of_transition_target_table;

  StateIdNumber number_of_states;
  TransitionNumber number_of_transitions;

  bool weighted;
  bool deterministic;
  bool input_deterministic;
  bool minimized;
  bool cyclic;
  bool has_epsilon_epsilon_transitions;
  bool has_input_epsilon_transitions;
  bool has_input_epsilon_cycles;
  bool has_unweighted_input_epsilon_cycles;

  void read_property(bool &property, FILE * f)
  {
    unsigned int prop;
    size_t ret;
    ret = fread(&prop,sizeof(unsigned int),1,f);
    (void)ret;
    if (prop == 0)
      {
    property = false;
    return;
      }
    else
      {
    property = true;
    return;
      }
  }

 public:
  TransducerHeader(FILE * f)
    {
      skip_hfst3_header(f);
    
      // The silly compiler complains about not catching the return value
      // of fread(). Hence this dummy variable is needed.
      size_t val;

      val = fread(&number_of_input_symbols,sizeof(SymbolNumber),1,f);
      val = fread(&number_of_symbols,sizeof(SymbolNumber),1,f);

      val = fread(&size_of_transition_index_table,sizeof(TransitionTableIndex),1,f);
      val = fread(&size_of_transition_target_table,sizeof(TransitionTableIndex),1,f);

      val = fread(&number_of_states,sizeof(StateIdNumber),1,f);
      val = fread(&number_of_transitions,sizeof(TransitionNumber),1,f);
      
      (void)val;

      read_property(weighted,f);

      read_property(deterministic,f);
      read_property(input_deterministic,f);
      read_property(minimized,f);
      read_property(cyclic,f);
      read_property(has_epsilon_epsilon_transitions,f);
      read_property(has_input_epsilon_transitions,f);
      read_property(has_input_epsilon_cycles,f);
      read_property(has_unweighted_input_epsilon_cycles,f);
    }

  void skip_hfst3_header(FILE * f);

  SymbolNumber symbol_count(void)
  { return number_of_symbols; }

  SymbolNumber input_symbol_count(void)
  { return number_of_input_symbols; }
  TransitionTableIndex index_table_size(void)
  { return size_of_transition_index_table; }

  TransitionTableIndex target_table_size(void)
  { return size_of_transition_target_table; }

  bool probe_flag(HeaderFlag flag)
  {
    switch (flag) {
    case Weighted: return weighted;
    case Deterministic: return deterministic;
    case Input_deterministic: return input_deterministic;
    case Minimized: return minimized;
    case Cyclic: return cyclic;
    case Has_epsilon_epsilon_transitions: return has_epsilon_epsilon_transitions;
    case Has_input_epsilon_transitions: return has_input_epsilon_transitions;
    case Has_input_epsilon_cycles: return has_input_epsilon_cycles;
    case Has_unweighted_input_epsilon_cycles: return has_unweighted_input_epsilon_cycles;
    }
    return false;
  }
};

class TransducerAlphabet
{
public:
    TransducerAlphabet(FILE *f,SymbolNumber symbol_number);
    ~TransducerAlphabet();
  
    KeyTable * get_key_table(void) { return kt; }

    SymbolNumber get_state_size(void) { return (SymbolNumber) feature_bucket.size(); }

private:
    void get_next_symbol(FILE *f, SymbolNumber k);

    void put_sym(SymbolNumber k, const char *p)
    {
        char *pOld = const_cast<char *>(kt->operator[](k));
        if (pOld != NULL)
        {
            free(pOld);
        }
        kt->operator[](k) = strdup(p);
    }

    SymbolNumber number_of_symbols;
    KeyTable *kt;

    char *line;

    std::map<std::string, SymbolNumber> feature_bucket;
    SymbolNumber feat_num;
};

class LetterTrie;
typedef std::vector<LetterTrie *> LetterTrieVector;

class LetterTrie
{
public:
    LetterTrie();
    ~LetterTrie();

    void add_string(const char * p,SymbolNumber symbol_key);

    SymbolNumber find_key(const char ** p);

private:
    LetterTrieVector letters;
    SymbolNumberVector symbols;
};

class Encoder {

 private:
  SymbolNumber number_of_input_symbols;
  LetterTrie letters;
  SymbolNumberVector ascii_symbols;

  void read_input_symbols(KeyTable * kt);

 public:
 Encoder(KeyTable * kt, SymbolNumber input_symbol_count):
  number_of_input_symbols(input_symbol_count),
    ascii_symbols(UCHAR_MAX,NO_SYMBOL_NUMBER)
      {
    read_input_symbols(kt);
      }
  
  SymbolNumber find_key(const char ** p);
};

/*
 * BEGIN old transducer.h
 */

typedef std::vector<std::string> DisplayVector;
typedef std::set<std::string> DisplaySet;

/*
 * BEGIN old transducer-weighted.h
 */

typedef float Weight;
const Weight INFINITE_WEIGHT = static_cast<float>(NO_TABLE_INDEX);

class TransitionWIndex;
class TransitionW;

typedef std::multimap<Weight, std::string> DisplayMultiMap;
typedef std::map<std::string, Weight> DisplayMap;

typedef std::vector<TransitionWIndex*> TransitionWIndexVector;
typedef std::vector<TransitionW*> TransitionWVector;

class TransitionWIndex
{
 private:
  SymbolNumber input_symbol;
  TransitionTableIndex first_transition_index;
  
 public:
  
  // Each TransitionIndex has an input symbol and a target index.
  static const size_t SIZE = 
    sizeof(SymbolNumber) + sizeof(TransitionTableIndex);

 TransitionWIndex(SymbolNumber input,
          TransitionTableIndex first_transition):
    input_symbol(input),
    first_transition_index(first_transition)
    {}

  bool matches(SymbolNumber s);
  
  TransitionTableIndex target(void)
  {
    return first_transition_index;
  }
  
  bool final(void)
  {
      return input_symbol == NO_SYMBOL_NUMBER &&
      first_transition_index != NO_TABLE_INDEX;
  }
  
  Weight final_weight(void)
  {
      union to_weight
      {
      TransitionTableIndex i;
      Weight w;
      } weight;
      weight.i = first_transition_index;
      return weight.w;
  }
  
  SymbolNumber get_input(void)
  {
    return input_symbol;
  }
};

class TransitionW
{
 private:
  SymbolNumber input_symbol;
  SymbolNumber output_symbol;  
  TransitionTableIndex target_index;
  Weight transition_weight;

 public:

  // Each transition has an input symbol an output symbol and 
  // a target index, as well as a weight.
  static const size_t SIZE = 
    2 * sizeof(SymbolNumber) + sizeof(TransitionTableIndex) + sizeof(Weight);

 TransitionW(SymbolNumber input,
         SymbolNumber output,
         TransitionTableIndex target,
         Weight w):
    input_symbol(input),
    output_symbol(output),
    target_index(target),
    transition_weight(w)
    {}

 TransitionW():
    input_symbol(NO_SYMBOL_NUMBER),
    output_symbol(NO_SYMBOL_NUMBER),
    target_index(NO_TABLE_INDEX),
    transition_weight(INFINITE_WEIGHT)
    {}

  bool matches(SymbolNumber s);

  TransitionTableIndex target(void)
  {
    return target_index;
  }

  SymbolNumber get_output(void)
  {
    return output_symbol;
  }

  SymbolNumber get_input(void)
  {
    return input_symbol;
  }
  
  Weight get_weight(void)
  {
    return transition_weight;
  }

  bool final(void)
  {
      return input_symbol == NO_SYMBOL_NUMBER &&
      output_symbol == NO_SYMBOL_NUMBER &&
      target_index == 1;
  }
};

class IndexTableReaderW
{
private:
    TransitionTableIndex number_of_table_entries;
    char *TableIndices;
    TransitionWIndexVector indices;
    size_t table_size;
  
    void get_index_vector(void);

public:
    IndexTableReaderW(FILE *f, TransitionTableIndex index_count)
    :
        TableIndices(NULL),
        number_of_table_entries(index_count)
    {
        table_size = number_of_table_entries * TransitionWIndex::SIZE;
        TableIndices = (char *)(malloc(table_size));

        // This dummy variable is needed, since the compiler complains
        // for not catching the return value of fread().
        size_t dummy_number_of_bytes;

        dummy_number_of_bytes = fread(TableIndices, table_size, 1, f);
        (void)dummy_number_of_bytes;
        get_index_vector();
    }

    ~IndexTableReaderW()
    {
        for (auto itr = indices.begin(); itr != indices.end(); ++itr) {
            TransitionWIndex *pT = *itr;
            delete pT;
        }
        if (TableIndices != NULL)
        {
            free(TableIndices);
        }
    }
  
  bool get_finality(TransitionTableIndex i)
  {
    return indices[i]->final();
  }
  
  TransitionWIndex * at(TransitionTableIndex i)
  {
    return indices[i];
  }
  
  TransitionWIndexVector &operator() (void)
    { return indices; }
};

class TransitionTableReaderW
{

 private:
  TransitionTableIndex number_of_table_entries;
  char * TableTransitions;
  TransitionWVector transitions;
  size_t table_size;
  
  TransitionTableIndex position;
  
  void get_transition_vector(void);

 public:
 TransitionTableReaderW(FILE * f,
                   TransitionTableIndex transition_count):
  number_of_table_entries(transition_count),
    position(0)
      {
    table_size = number_of_table_entries*TransitionW::SIZE;
    TableTransitions = (char*)(malloc(table_size));
    size_t bytes;
    bytes = fread(TableTransitions,table_size,1,f);
    (void)bytes;
    get_transition_vector();
      }

  ~TransitionTableReaderW()
  {
      for (auto itr = transitions.begin(); itr != transitions.end(); ++itr) {
          TransitionW *pT = *itr;
          delete pT;
      }

      free(TableTransitions);
  }

  void Set(TransitionTableIndex pos);

  TransitionW * at(TransitionTableIndex i)
  {
    return transitions[i - TRANSITION_TARGET_TABLE_START];
  }

  void Next(void)
  {
    ++position;
  }
  
  bool Matches(SymbolNumber s);

  TransitionTableIndex get_target(void)
  {
    return transitions[position]->target();
  }

  SymbolNumber get_output(void)
  {
    return transitions[position]->get_output();
  }

  SymbolNumber get_input(void)
  {
    return transitions[position]->get_input();
  }

  bool get_finality(TransitionTableIndex i);

  TransitionWVector &operator() (void)
    { 
      return transitions; 
    }
};

class TransducerW
{
protected:
  TransducerHeader header;
  std::shared_ptr<TransducerAlphabet> pAlphabet;
  KeyTable * keys;
  IndexTableReaderW index_reader;
  TransitionTableReaderW transition_reader;
  Encoder encoder;
  DisplayMultiMap display_map;

  SymbolNumber *output_string;

  static const TransitionTableIndex START_INDEX = 0;

  std::vector<const char*> symbol_table;

  TransitionWIndexVector &indices;

  TransitionWVector &transitions;

  Weight current_weight;

  void set_symbol_table(void);

  virtual void try_epsilon_transitions(SymbolNumber * input_symbol,
                       SymbolNumber * output_symbol,
                       SymbolNumber * original_output_string,
                       TransitionTableIndex i);
  
  void try_epsilon_indices(SymbolNumber * input_symbol,
                   SymbolNumber * output_symbol,
                   SymbolNumber * original_output_string,
                   TransitionTableIndex i);

  void find_transitions(SymbolNumber input,
            SymbolNumber * input_symbol,
            SymbolNumber * output_symbol,
            SymbolNumber * original_output_string,
            TransitionTableIndex i);

  void find_index(SymbolNumber input,
          SymbolNumber * input_symbol,
          SymbolNumber * output_symbol,
          SymbolNumber * original_output_string,
          TransitionTableIndex i);

  virtual void note_analysis(SymbolNumber * whole_output_string);

  bool final_transition(TransitionTableIndex i)
  {
    return transitions[i]->final();
  }
  
  bool final_index(TransitionTableIndex i)
  {
    return indices[i]->final();
  }

  void get_analyses(SymbolNumber * input_symbol,
            SymbolNumber * output_symbol,
            SymbolNumber * original_output_string,
            TransitionTableIndex i);

  Weight get_final_index_weight(TransitionTableIndex i) {
    return indices[i]->final_weight();
  }

  Weight get_final_transition_weight(TransitionTableIndex i) {
    return transitions[i]->get_weight();
  }

public:
    TransducerW(FILE *f, TransducerHeader h, std::shared_ptr<TransducerAlphabet> pA);
    ~TransducerW();

  const DisplayMultiMap& get_display_map()
  {
    return display_map;
  }

  void clear_display_map()
  {
    display_map.clear();
  }

  KeyTable * get_key_table(void)
  {
    return keys;
  }

  void analyze(SymbolNumber * input_string)
  {
    get_analyses(input_string,output_string,output_string,START_INDEX);
  }


  SymbolNumber find_next_key(const char ** p)
  {
    return encoder.find_key(p);
  }

  virtual void printAnalyses(std::string prepend);
private:
  TransducerW operator=(const TransducerW&)
  {
      assert(false);
  }
};
