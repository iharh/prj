/*     Foma: a finite-state toolkit and library.                             */
/*     Copyright c 2008-2015 Mans Hulden                                     */

/*     This file is part of foma.                                            */

/*     Foma is free software: you can redistribute it and/or modify          */
/*     it under the terms of the GNU General Public License version 2 as     */
/*     published by the Free Software Foundation.                            */

/*     Foma is distributed in the hope that it will be useful,               */
/*     but WITHOUT ANY WARRANTY; without even the implied warranty of        */
/*     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         */
/*     GNU General Public License for more details.                          */

/*     You should have received a copy of the GNU General Public License     */
/*     along with foma.  If not, see <http://www.gnu.org/licenses/>.         */

#include "foma.h"
#include <stdlib.h>
#include <string.h>

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
