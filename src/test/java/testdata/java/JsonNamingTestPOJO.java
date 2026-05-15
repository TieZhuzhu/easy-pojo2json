package testdata.java;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * LowerCamelCase 命名策略测试样例。
 * 
 * @author August Lee
 * @see LowerCamelCaseStrategyTestPOJO
 * @since 2026-05-15 15:29:08
 *
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
class LowerCamelCaseStrategyTestPOJO {
    private String firstName;
}

/**
 * UpperCamelCase 命名策略测试样例。
 * 
 * @author August Lee
 * @see UpperCamelCaseStrategyTestPOJO
 * @since 2026-05-15 15:29:08
 *
 */
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
class UpperCamelCaseStrategyTestPOJO {
    private String firstName;
}

/**
 * SnakeCase 命名策略测试样例。
 * 
 * @author August Lee
 * @see SnakeCaseStrategyTestPOJO
 * @since 2026-05-15 15:29:08
 *
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
class SnakeCaseStrategyTestPOJO {
    private String firstName;
}

/**
 * UpperSnakeCase 命名策略测试样例。
 * 
 * @author August Lee
 * @see UpperSnakeCaseStrategyTestPOJO
 * @since 2026-05-15 15:29:08
 *
 */
@JsonNaming(PropertyNamingStrategies.UpperSnakeCaseStrategy.class)
class UpperSnakeCaseStrategyTestPOJO {
    private String firstName;
}

/**
 * KebabCase 命名策略测试样例。
 * 
 * @author August Lee
 * @see KebabCaseStrategyTestPOJO
 * @since 2026-05-15 15:29:08
 *
 */
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
class KebabCaseStrategyTestPOJO {
    private String firstName;
}

/**
 * LowerCase 命名策略测试样例。
 * 
 * @author August Lee
 * @see LowerCaseStrategyTestPOJO
 * @since 2026-05-15 15:29:08
 *
 */
@JsonNaming(PropertyNamingStrategies.LowerCaseStrategy.class)
class LowerCaseStrategyTestPOJO {
    private String firstName;
}

/**
 * LowerDotCase 命名策略测试样例。
 * 
 * @author August Lee
 * @see LowerDotCaseStrategyTestPOJO
 * @since 2026-05-15 15:29:08
 *
 */
@JsonNaming(PropertyNamingStrategies.LowerDotCaseStrategy.class)
class LowerDotCaseStrategyTestPOJO {
    private String firstName;
}
