/*     Foma: a finite-state toolkit and library.                             */
/*     Copyright c 2008-2015 Mans Hulden                                     */

/*     This file is part of foma.                                            */

/*     Foma is free software: you can redistribute it and/or modify          */
/*     it under the terms of the GNU General Public License version 2 as     */
/*     published by the Free Software Foundation. */

/*     Foma is distributed in the hope that it will be useful,               */
/*     but WITHOUT ANY WARRANTY; without even the implied warranty of        */
/*     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         */
/*     GNU General Public License for more details.                          */

/*     You should have received a copy of the GNU General Public License     */
/*     along with foma.  If not, see <http://www.gnu.org/licenses/>.         */

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
