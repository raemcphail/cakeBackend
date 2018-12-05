package database;

public interface DBConstants {
    String ALLERGY_TABLE                =  "`ALLERGY`";

    String CHANNEL_TABLE                =  "`CHANNEL`";
    String CHANNEL_CONTAINS_TABLE       =  "`CHANNEL_CONTAINS`";
    String CHANNEL_TAGS_TABLE           =  "`CHANNEL_TAGS`";
    String HOSTS_TABLE                  =  "`HOSTS`";

    String END_USER_TABLE               =  "`END_USER`";
    String CURATOR_TABLE                =  "`CURATOR`";
    String STD_USER_TABLE               =  "`STD_USER`";

    String DEPENDANT_TABLE              =  "`DEPENDANTS`";

    String SUBSCRIPTIONS_TABLE          =  "`SUBSCRIPTIONS`";
    String USER_MEALS_TABLE             =  "`USER_MEALS`";
    String USER_RECIPES_TABLE           =  "`USER_RECIPES`";
    String USER_INGREDIENTS_TABLE       =  "`USER_INGREDIENTS`";

    String MEAL_PLAN_TABLE              =  "`MEAL_PLAN`";
    String MEAL_PLAN_CONTAINS_TABLE     =  "`MEAL_PLAN_CONTAINS`";

    String MEAL_TABLE                   =  "`MEAL`";
    String MEAL_TAGS_TABLE              =  "`MEAL_TAGS`";
    String MEAL_CONTAINS_TABLE          =  "`MEAL_CONTAINS`";

    String RECIPE_TABLE                 =  "`RECIPE`";
    String RECIPE_TAGS_TABLE            =  "`RECIPE_TAGS`";
    String RECIPE_CONTAINS_TABLE        =  "`RECIPE_CONTAINS`";
    String RECIPE_REQUIRES_EQUIPT_TABLE =  "`RECIPE_REQUIRES_EQUIPT`";

    String EQUIPTMENT_TABLE             =  "`EQUIPMENT`";

    String INGREDIENT_TABLE             =  "`INGREDIENT`";
    String SUBSTITUTE_TABLE             =  "`SUBSTITUTE`";
}
