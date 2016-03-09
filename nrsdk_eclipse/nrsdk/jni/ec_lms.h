/*
	ec_lms.h

	NLMS echo canceller header file

*/

#define		LMSSIZE			  640	  // Length of FIR filter 40ms
#define		LMSHANGSET		  520	  // Hangover time, 65 ms

#define		K0_8			26214	  // 0.800 Q15	
#define		K0_005			  164     // 0.005 Q15
#define		K0_995			32604     // 0.995 Q15

#define		LMSATTEN0		32767     // 1.000 Q15
#define		LMSATTEN2		26000     // 0.790 Q15
#define		LMSATTEN3		22938     // 0.700 Q15
#define		LMSATTEN6		16384     // 0.500 Q15
#define		LMSATTEN10		10362     // 0.316 Q15
#define		LMSATTEN15		 5827     // 0.178 Q15
#define		LMSATTEN20		 3000     // 0.092 Q15
#define		LMSATTEN24		 2000     // 0.061 Q15
#define		LMSATTEN26		 1638     // 0.050 Q15
#define		LMSATTEN30		 1036     // 0.032 Q15


struct ec_lms_struct {
	short	TxMax_128;
	short	TxMag;			// Average magnitude of Tx signal
	short	RxMag1;			// Average magnitude of Rx signal, before ec
	short	RxMax1;			// Maximum magnitude of Rx signal, before ec
	short	RxMag2;			// Average magnitude of Rx signal, after ec
	short	RxMax2;			// Maximum magnitude of Rx signal, after ec
	short	HangCount;		// Counter of no-adaptation time left
	short	Timer;			// 0.5 second timeout to reduce step size
	short	Shift;			// {-1, 0, +1}, control of step size
	short	Coeff[LMSSIZE];		// filter coefficients, backward, a[LMSSIZE-1]...a[0]
	short	stat[LMSSIZE];		// filter history, x[n-LMSSIZE]...x[n-1]
};
