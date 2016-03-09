/* fxpop.h

  Prototypes of arithmetic operators

  Wenhui Jia	6/26/2000
*/

typedef short	Word16;
typedef long	Word32;

/* Constants */
#define MAX_WORD32 (Word32)0x7fffffffL
#define MIN_WORD32 (Word32)0x80000000L
#define MAX_WORD16 (Word16)0x7fff
#define MIN_WORD16 (Word16)0x8000

/* Functions */
Word16 add(Word16 var1, Word16 var2);     /* 16-b add */
Word16 sub(Word16 var1, Word16 var2);     /* 16-b sub */
Word16 mult(Word16 var1, Word16 var2);    /* 16-b mult */
Word16 mult_r(Word16 var1, Word16 var2);  /* 16-b mult with round */
Word16 abs_s(Word16 var1);                /* 16-b abs */
Word16 div16(Word16 var1, Word16 var2);     /* 16-b division */
Word32 L_mult(Word16 var1, Word16 var2);  /* 16-b mult --> 32-b */
Word32 L_add(Word32 L_var1, Word32 L_var2);   /* 32-b add */
Word32 L_sub(Word32 L_var1, Word32 L_var2);   /* 32-b sub */
Word16 norm_l(Word32 L_var1);                   /* 32-b norm */
Word32 L_abs(Word32 L_var1);