package ee;

import org.jooq.DSLContext;
import org.jooq.h2.generated.tables.Testtab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

@SpringBootApplication(scanBasePackages = "ee", exclude = {
  DataSourceAutoConfiguration.class,
  DataSourceTransactionManagerAutoConfiguration.class
})
public class Example implements CommandLineRunner {

  private final DSLContext create;

  @Autowired
  public Example(DSLContext dslContext) {
    this.create = dslContext;
  }

  @Override
  public void run(String... args) throws Exception {
    create.insertInto(Testtab.TESTTAB).set(Testtab.TESTTAB.DATAARR, new Integer[]{1, 2}).execute();
    Domain d = create.select().from(Testtab.TESTTAB).fetchOne().into(Domain.class);
    System.out.println(d.getDataarr());
    System.exit(0);
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Example.class, args);
  }
}