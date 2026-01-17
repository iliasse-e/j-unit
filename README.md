## Les méthodes de test

Une classe de test est simplement une classe déclarant des méthodes publiques sans paramètre et sans valeur de retour et qui sont annotées par @Test.

Une méthode de test contient :

- un ensemble d’instructions correspondant à la phase *arrange* (si nécessaire),

- un ensemble d’instructions correspondant à la phase *act* (qui se limite généralement à l’appel de la méthode à tester),

- un ensemble d’instructions correspondant à la phase *assert*.

L’exemple ci-dessous teste la méthode ``String.toUpperCase`` :

```java
public class StringTest {

  @Test
  public void upperCaseProduitUneChaineEnMajuscules() throws Exception {
    // Bloc arrange
    String s = "Bonjour le monde";

    // Bloc act
    String maj = s.toUpperCase();

    // Bloc assert
    assertEquals("BONJOUR LE MONDE", maj);
  }

}
```


## Les assertions

La classe Assert est une classe outil contenant des méthodes statiques pour déclarer des assertions. Ces méthodes permettent de vérifier la valeur d’un paramètre ou de comparer deux valeurs passées en paramètres. Si l’assertion est fausse, ces méthodes produisent une exception de type ``AssertionError`` qui fait échouer le test.

    assertTrue(boolean condition) | assertFalse(boolean condition)
**

    assertEquals(Object expected, Object actual) | assertNotEquals(Object expected, Object actual)
*Compare les deux paramètres pour vérifier qu’ils sont égaux.*
    
    assertSame(Object expected, Object actual) | assertNotSame(Object expected, Object actual)
  *Vérifie que les deux objets passés en paramètre sont en fait le même objet (en utilisant l’opérateur ==)*

    assertNull(Object actual) | assertNotNull(Object actual)

**

    fail()


## Les fixtures

Pour réaliser un test, il est parfois nécessaire un grand nombre d’objets et de préparer le SUT (System Under Test). Plutôt que d’écrire le code nécessaire au début d’un test (au risque de le rendre moins lisible), on préfère écrire ce code dans une classe à part ou une méthode à part. Dans ce cas, on qualifie ce nouvel objet ou cette nouvelle méthode de **fixtures**.

Avec JUnit, il est possible d’exécuter des méthodes avant et après chaque test pour allouer et désallouer des ressources nécessaires à l’exécution des tests. On déclare pour cela des méthodes publiques sans paramètre annotées avec ``@Before`` ou ``@After``.

```java
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExempleTest {

    @BeforeClass
    public static void setupBeforeAll() {
        System.out.println(">>> @BeforeClass : Exécuté une seule fois AVANT tous les tests");
    }

    @AfterClass
    public static void teardownAfterAll() {
        System.out.println(">>> @AfterClass : Exécuté une seule fois APRÈS tous les tests");
    }

    @Before
    public void setupBeforeEach() {
        System.out.println(" -> @Before : Exécuté AVANT chaque test");
    }

    @After
    public void teardownAfterEach() {
        System.out.println(" -> @After : Exécuté APRÈS chaque test");
    }

    @Test
    public void testAddition() {
        System.out.println("    [TEST] testAddition");
        int result = 2 + 3;
        assert result == 5;
    }

    @Test
    public void testMultiplication() {
        System.out.println("    [TEST] testMultiplication");
        int result = 4 * 2;
        assert result == 8;
    }
}

```
> [!NOTE]
> Il est également possible de déclarer des méthodes static annotées avec ``@BeforeClass`` ou ``@AfterClass``. Ces méthodes ne sont appelées qu’une seule fois respectivement avant ou après l’ensemble des méthodes de test de la classe.

## Tester les exceptions

Grâce aux tests unitaires, il est également plus facile de tester les cas non nominaux qui se traduisent la plupart du temps par la production d’une exception en Java. Si on désire tester un cas d’erreur par exemple, cela signifie que le test sera ok si une exception précise est produite lors de la phase *act*.

### 4 manières :

```java
@Test
public void parseIntThrowsExceptionWhenNotANumber() throws Exception {
  try {
    Integer.parseInt("not a number");
    fail("NumberFormatException expected");
  } catch (NumberFormatException e) {
  }
}
```

Dans l’exemple ci-dessus, on utilise une structure ``try`` ... ``catch`` pour attraper l’exception qui est attendue. Dans le bloc ``try``, l’appel à la méthode ``Assert.fail`` pour faire échouer le test si jamais la phase act (c’est-à-dire l’appel à Integer#parseInt) n’a pas produit d’exception. Cette façon d’écrire le test est simple mais rend le test parfois difficile à lire à cause de la présence des blocs ``try`` ... ``catch`` et de l’absence d’une phase ``assert`` remplacée par l’appel à ``Assert.fail``.

```java
@Test(expected = NumberFormatException.class)
public void parseIntThrowsExceptionWhenNotANumber() throws Exception {
  Integer.parseInt("not a number");
}
```
Dans l’exemple ci-dessus, on utilise l’attribut expected de l’annotation ``@Test`` qui permet d’indiquer que l’on s’attend à ce que le test échoue à cause de la propagation d’une exception (si ce n’est pas le cas, le test sera considéré en échec). Cette façon d’écrire le test est plus simple que précédemment mais elle peut être difficile à comprendre car la phase *assert* n’est pas explicite

```java
@Rule
public ExpectedException expectedException = ExpectedException.none();

@Test
public void parseIntThrowsExceptionWhenNotANumber() throws Exception {
  expectedException.expect(NumberFormatException.class);

  Integer.parseInt("not a number");
}
```

Cette manière est ``@Deprecated`` depuis J-Unit 4.1.3

```java
@Test
public void parseIntThrowsExceptionWhenNotANumber() throws Exception {
  assertThrows(NumberFormatException.class, () -> {
    Integer.parseInt("not a number");
  });
}

@Test
void testExceptionMessage() {
  Exception ex = assertThrows(IllegalArgumentException.class, () -> {
      throw new IllegalArgumentException("Montant invalide");
  });

  assert ex.getMessage().equals("Montant invalide");
}

```

Avec l’introduction des lambdas depuis Java 8, il est plus direct d’encapsuler un appel d’un code produisant une exception dans une fonction anonyme. On utilise pour cela ``Assert.assertThrows`` en précisant le type de l’exception attendue. Si cette approche est élégante, elle mélange tout de même les codes de la phase `*act*` et de la phase `*assert*`.

## Utilisation de doublure

Parfois, il est utile de contrôler l’environnement de test d’un objet ou d’une collaboration d’objets. Pour cela, on peut faire appel à des doublures qui vont se substituer lors des tests aux objets réellement utilisés lors de l’exécution de l’application dans un environnement de production.

#### Simulateur

  Un simulateur fournit une implémentation alternative d’un sous-système. Un simulateur remplace un sous-système qui n’est pas disponible pour l’environnement de test. Par exemple, on peut remplacer un système de base de données par une implémentation simplifiée en mémoire.

#### Fake object

  Un fake object permet de remplacer un sous-système dont il est difficile de garantir le comportement. Le comportement du fake object est défini par le test et est donc déterministe. Par exemple, si un objet dépend des informations retournées par un service Web, il est souhaitable de remplacer pour les tests l’implémentation du client par une implémentation qui retournera une réponse déterminée par le test lui-même.

#### Mock object

  Un objet mock est proche d’un fake object sauf qu’un objet mock est également capable de faire des assertions sur les méthodes qui sont appelées et les paramètres qui sont transmis à ces méthodes.
