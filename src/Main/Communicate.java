package Main;

public interface Communicate {
	// connection
		static final int MIN_PORT 				= 4200;
		static final int MAX_PORT 				= 4212;
/*		FORMAT: 	0xACTU
		A	- unused so far
		C 	- command
		TU	- type
	[	T0 	- user type		]
*/
		
/*********************
 *       MASKS       *
 *********************/
		//COMMAND MAKS
		static final int COMMAND_MASK			= 0x0F00;
		
		static final int CONTROL				= 0xF0FF;
		static final int GET					= 0xF1FF;
		static final int SEARCH					= 0xF2FF;
		static final int ADD					= 0xF3FF;
		
		//TYPE MASKS
		static final int TYPE_MASK				= 0x00FF;
		
		static final int END_USER 				= 0xFF00;
		static final int STD_USER 				= 0xFF10;
		static final int CUR_USER 				= 0xFF20;
		static final int DEPENDANT 				= 0xFF30;
		
		static final int INGREDIENTS		 	= 0xFF02;
		static final int RECIPES 				= 0xFF03;
		static final int MEALS 					= 0xFF04;
		
		
/********************
 * 	   COMMANDS		*
 ********************/		
		// CONTROL									00
		static final int INVALID				= 0x0000;
		
		static final int DB_ERROR				= 0x0001;
		static final int DB_SUCCESS				= 0x0002;
		
		static final int CONNECTED				= 0x0003;
		static final int DISCONNECT				= 0x0004;
		
		// GET_USER_THINGS							01
		static final int GET_USER_DEPENDANTS	= 0x0103;
		static final int GET_USER_INGREDIENT	= 0x0102;
		static final int GET_USER_RECIPES		= 0x0103;
		static final int GET_USER_MEALS			= 0x0104;
		
		// SEARCH									02
		static final int SEARCH_ALL_USERS 		= 0x0200;
		static final int SEARCH_STD_USERS 		= 0x0210;
		static final int SEARCH_CUR_USERS 		= 0x0220;
		
		static final int SEARCH_INGREDIENTS 	= 0x0202;
		static final int SEARCH_RECIPES 		= 0x0203;
		static final int SEARCH_MEALS 			= 0x0204;
		
		// ADD										03
		static final int ADD_ANY_USER 			= 0x0300;
		static final int ADD_STD_USERS 			= 0x0310;
		static final int ADD_CUR_USERS 			= 0x0320;
		
		static final int ADD_INGREDIENTS	 	= 0x0302;
		static final int ADD_RECIPES 			= 0x0303;
		static final int ADD_MEALS 				= 0x0304;

		
		// program attributes
		static final String PROGRAM_NAME = "Recipe App Backend";
		static final int minCommand = 0x40, maxCommand = 0x50;
		
		
		
}
