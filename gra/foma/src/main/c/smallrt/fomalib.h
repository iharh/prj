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

#ifdef  __cplusplus
extern "C" {
#endif
#include <stdio.h>
#include <inttypes.h>
#include <string.h>

#ifdef ORIGINAL
#define INLINE inline

#define FEXPORT __attribute__((visibility("default")))
#else // #ifdef ORIGINAL
  #ifdef _MSC_VER
    #define FEXPORT __declspec(dllexport)
    #define INLINE
  #else
    #define INLINE inline
    #define FEXPORT __attribute__((visibility("default")))
  #endif
#endif // #ifdef ORIGINAL

#include "fomalibconf.h"

#ifdef  __cplusplus
}
#endif
