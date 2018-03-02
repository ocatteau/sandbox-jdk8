package com.example.project;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.example.project.bean.Bar;
import com.example.project.bean.Foo;
import com.example.project.bean.Person;
import org.assertj.core.data.MapEntry;
import org.junit.Test;

public class StreamsTest {
	private final Person max = new Person("Max", 18);
	private final Person david = new Person("David", 12);
	private final Person pamela = new Person("Pamela", 23);
	private final Person peter = new Person("Peter", 23);

	private Foo foo1 = new Foo("Foo1");

	private Bar bar1Foo1 = new Bar("Bar1 <- Foo1");
	private Bar bar2Foo1 = new Bar("Bar2 <- Foo1");
	private Bar bar3Foo1 = new Bar("Bar3 <- Foo1");
	private Foo foo2 = new Foo("Foo2");
	private Bar bar1Foo2 = new Bar("Bar1 <- Foo2");
	private Bar bar2Foo2 = new Bar("Bar2 <- Foo2");
	private Bar bar3Foo2 = new Bar("Bar3 <- Foo2");
	private Foo foo3 = new Foo("Foo3");
	private Bar bar1Foo3 = new Bar("Bar1 <- Foo3");
	private Bar bar2Foo3 = new Bar("Bar2 <- Foo3");
	private Bar bar3Foo3 = new Bar("Bar3 <- Foo3");


	@Test
	public void stream_withFilterMapSortedForEach() throws Exception {
		asList("a1", "a2", "b1", "c2", "c1").stream()
				.filter(s -> s.startsWith("c"))
				.map(String::toUpperCase)
				.sorted()
				.forEach(System.out::println);

		// C1
		// C2
	}

	@Test
	public void stream_findFirst() throws Exception {
		List<String> result = new ArrayList<>();

		asList("a1", "a2", "a3")
				.stream()
				.findFirst()
				.ifPresent(s -> {
					result.add(s);
					System.out.println(s);
				});
		// a1
		assertThat(result).containsExactly("a1");
	}

	@Test
	public void stream_streamOf() throws Exception {
		Stream<String> stream = Stream.of("a1", "a2", "a3");
		Optional<String> firstOptional = stream.findFirst();
		firstOptional.ifPresent(System.out::println);
		// a1
	}

	@Test
	public void stream_intStream() throws Exception {
		IntStream intStream = IntStream.range(1, 4);
		intStream.forEach(System.out::println);
		// 1
		// 2
		// 3
	}

	@Test
	public void stream_arrayStream() throws Exception {
		IntStream intStream = Arrays.stream(new int[]{1, 2, 3});
		IntStream mappedIntStream = intStream.map(n -> 2 * n + 1);
		OptionalDouble averageOptional = mappedIntStream.average();
		averageOptional.ifPresent(System.out::println);  // 5.0
	}

	@Test
	public void stream_mapToInt() throws Exception {
		Stream<String> stream = Stream.of("a1", "a2", "a3");
		Stream<String> substringStream = stream.map(s -> s.substring(1));
		substringStream.mapToInt(Integer::parseInt)
				.max()
				.ifPresent(System.out::println);  // 3
	}

	@Test
	public void stream_mapToObj() throws Exception {
		IntStream.range(1, 4)
				.mapToObj(i -> "a" + i)
				.forEach(System.out::println);
		// a1
		// a2
		// a3
	}

	@Test
	public void stream_mapToIntThenMapToObj() throws Exception {
		Stream.of(1.0, 2.0, 3.0)
				.mapToInt(Double::intValue)
				.mapToObj(i -> "a" + i)
				.forEach(System.out::println);
		// a1
		// a2
		// a3
	}

	@Test
	public void stream_mapToObjWithPeek() throws Exception {
		IntStream.range(1, 4)
				.mapToObj(i -> new Foo("Foo" + i))
				.peek(f -> IntStream.range(1, 4)
						.mapToObj(i -> new Bar("Bar" + i + " <- " + f.name))
						.forEach(f.bars::add))
				.forEach(foo -> System.out.println(foo));
//		Foo{name='Foo1', bars=[Bar{name='Bar1 <- Foo1'}, Bar{name='Bar2 <- Foo1'}, Bar{name='Bar3 <- Foo1'}]}
//		Foo{name='Foo2', bars=[Bar{name='Bar1 <- Foo2'}, Bar{name='Bar2 <- Foo2'}, Bar{name='Bar3 <- Foo2'}]}
//		Foo{name='Foo3', bars=[Bar{name='Bar1 <- Foo3'}, Bar{name='Bar2 <- Foo3'}, Bar{name='Bar3 <- Foo3'}]}
	}

		@Test
	public void stream_filter_withoutTerminalOperation_nothingExecuted() throws Exception {
		List<String> result = new ArrayList<>();
		Stream.of("d2", "a2", "b1", "b3", "c")
				.filter(s -> {
					System.out.println("filter: " + s);
					result.add(s);
					return true;
				});

		assertThat(result).isEmpty();
	}

	@Test
	public void stream_filter_withTerminalOperation_executed() throws Exception {
		List<String> result = new ArrayList<>();
		Stream.of("d2", "a2", "b1", "b3", "c")
				.filter(s -> {
					System.out.println("filter: " + s);
					result.add(s);
					return true;
				})
				.forEach(s -> System.out.println("forEach: " + s));
		// filter:  d2
		// forEach: d2
		// filter:  a2
		// forEach: a2
		// filter:  b1
		// forEach: b1
		// filter:  b3
		// forEach: b3
		// filter:  c
		// forEach: c

		assertThat(result).containsExactly("d2", "a2", "b1", "b3", "c");
	}

	@Test
	public void stream_mapAnyMatch() throws Exception {
		boolean isMatching = Stream.of("d2", "a2", "b1", "b3", "c")
				.map(s -> {
					System.out.println("map: " + s);
					return s.toUpperCase();
				})
				.anyMatch(s -> {
					System.out.println("anyMatch: " + s);
					return s.startsWith("A");
				});

		// map:      d2
		// anyMatch: D2
		// map:      a2
		// anyMatch: A2
		assertThat(isMatching).isTrue();
	}

	@Test
	public void stream_orderMattersOnIntermediateOperations() throws Exception {
		Stream.of("d2", "a2", "b1", "b3", "c")
				.map(s -> {
					System.out.println("map: " + s);
					return s.toUpperCase();
				})
				.filter(s -> {
					System.out.println("filter: " + s);
					return s.startsWith("A");
				})
				.forEach(s -> System.out.println("forEach: " + s));
		// map:     d2
		// filter:  D2
		// map:     a2
		// filter:  A2
		// forEach: A2
		// map:     b1
		// filter:  B1
		// map:     b3
		// filter:  B3
		// map:     c
		// filter:  C

		Stream.of("d2", "a2", "b1", "b3", "c")
				.filter(s -> {
					System.out.println("filter: " + s);
					return s.startsWith("a");
				})
				.map(s -> {
					System.out.println("map: " + s);
					return s.toUpperCase();
				})
				.forEach(s -> System.out.println("forEach: " + s));
		// filter:  d2
		// filter:  a2
		// map:     a2
		// forEach: A2
		// filter:  b1
		// filter:  b3
		// filter:  c

		Stream.of("d2", "a2", "b1", "b3", "c")
				.sorted((s1, s2) -> {
					System.out.printf("sort: %s; %s\n", s1, s2);
					return s1.compareTo(s2);
				})
				.filter(s -> {
					System.out.println("filter: " + s);
					return s.startsWith("a");
				})
				.map(s -> {
					System.out.println("map: " + s);
					return s.toUpperCase();
				})
				.forEach(s -> System.out.println("forEach: " + s));
		// sort:    a2; d2
		// sort:    b1; a2
		// sort:    b1; d2
		// sort:    b1; a2
		// sort:    b3; b1
		// sort:    b3; d2
		// sort:    c; b3
		// sort:    c; d2
		// filter:  a2
		// map:     a2
		// forEach: A2
		// filter:  b1
		// filter:  b3
		// filter:  c
		// filter:  d2

		Stream.of("d2", "a2", "b1", "b3", "c")
				.filter(s -> {
					System.out.println("filter: " + s);
					return s.startsWith("a");
				})
				.sorted((s1, s2) -> {
					System.out.printf("sort: %s; %s\n", s1, s2);
					return s1.compareTo(s2);
				})
				.map(s -> {
					System.out.println("map: " + s);
					return s.toUpperCase();
				})
				.forEach(s -> System.out.println("forEach: " + s));
		// filter:  d2
		// filter:  a2
		// filter:  b1
		// filter:  b3
		// filter:  c
		// map:     a2
		// forEach: A2
	}

	@Test
	public void stream_cannotReuseStreamDirectly() throws Exception {
		Stream<String> stream =
				Stream.of("d2", "a2", "b1", "b3", "c")
						.filter(s -> s.startsWith("a"));

		boolean isMatching = stream.anyMatch(s -> true);// ok
		assertThat(isMatching).isTrue();

		try {
			stream.noneMatch(s -> true);   // exception
		} catch (IllegalStateException e) {
			assertThat(e.getMessage()).isEqualTo("stream has already been operated upon or closed");
		}
	}

	@Test
	public void stream_howToReuseStream() throws Exception {
		Supplier<Stream<String>> streamSupplier =
				() -> Stream.of("d2", "a2", "b1", "b3", "c")
						.filter(s -> s.startsWith("a"));

		boolean isMatching = streamSupplier.get().anyMatch(s -> true);// ok
		assertThat(isMatching).isTrue();

		boolean isNoneMatch = streamSupplier.get().noneMatch(s -> true);// ok
		assertThat(isNoneMatch).isFalse();
	}

	@Test
	public void stream_collectToList() throws Exception {
		List<Person> filtered =
				persons().stream()
						.filter(p -> p.name.startsWith("P"))
						.collect(Collectors.toList());

		System.out.println(filtered);    // [Peter, Pamela]
		assertThat(filtered).containsExactly(peter, pamela);
	}

	@Test
	public void stream_collectGroupBy() throws Exception {
		Map<Integer, List<Person>> personsByAge = persons().stream().collect(Collectors.groupingBy(p -> p.age));
		personsByAge.forEach((age, p) -> System.out.format("age %s: %s\n", age, p));
		assertThat(personsByAge).containsExactly(entry(18, asList(max)),
												 entry(23, asList(peter, pamela)),
												 entry(12, asList(david)));
	}

	@Test
	public void stream_collectJoining() throws Exception {
		String phrase = persons()
				.stream()
				.filter(p -> p.age >= 18)
				.map(p -> p.name)
				.collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));
		assertThat(phrase).isEqualTo("In Germany Max and Peter and Pamela are of legal age.");
	}

	@Test
	public void stream_collectToMap() throws Exception {
		// IllegalStateException when multiple keys without mergeFunction
		try {
			persons().stream().collect(Collectors.toMap(p -> p.age, p -> p.name));
		} catch (IllegalStateException e) {
			assertThat(e.getMessage()).isEqualTo("Duplicate key Peter");
		}

		// Works with mergeFunction
		Map<Integer, String> map = persons().stream()
				.collect(Collectors.toMap(
						p -> p.age,
						p -> p.name,
						(name1, name2) -> name1 + ";" + name2));

		assertThat(map).containsExactly(entry(18, max.name),
										entry(23, peter.name + ";" + pamela.name),
										entry(12, david.name));
	}

	@Test
	public void stream_customCollector() throws Exception {
		Collector<Person, StringJoiner, String> personNameCollector =
				Collector.of(
						() -> new StringJoiner(" | "),                              // supplier
						(joiner, person) -> joiner.add(person.name.toUpperCase()),  // accumulator
						(joiner1, joiner2) -> joiner1.merge(joiner2),               // combiner
						StringJoiner::toString);                                    // finisher

		String names = persons().stream().collect(personNameCollector);

		assertThat(names).isEqualTo("MAX | PETER | PAMELA | DAVID");
	}

	@Test
	public void stream_flatMap() throws Exception {
		List<Bar> actual = foos().stream()
				.flatMap(f -> f.bars.stream())
				.collect(Collectors.toList());
		
		assertThat(actual).containsExactly(bar1Foo1, bar2Foo1, bar3Foo1, bar1Foo2, bar2Foo2, bar3Foo2, bar1Foo3, bar2Foo3, bar3Foo3 );
	}

	@Test
	public void stream_reduce() throws Exception {
		Optional<Person> oldestPerson = persons().stream()
				.reduce((p1, p2) -> p1.age > p2.age ? p1 : p2);
		assertThat(oldestPerson.get()).isEqualTo(pamela);
	}

	@Test
	public void stream_reduceWithIdentityValue() throws Exception {
		Person person = persons().stream()
				.reduce(new Person("", 0), (p1, p2) -> {
					p1.name = new StringJoiner(" ").add(p1.name).add(p2.name).toString().trim();
					p1.age += p2.age;
					return p1;
				});
		assertThat(person.name).isEqualTo("Max Peter Pamela David");
		assertThat(person.age).isEqualTo(76);
	}

	@Test
	public void stream_reduceWithIdentityValueAndCombiner() throws Exception {
		Integer ageSum = persons().stream()
				.reduce(0,
						(sum, p) -> {
							System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
							return sum += p.age;
						},
						(sum1, sum2) -> {
							System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
							return sum1 + sum2;
						});
		assertThat(ageSum).isEqualTo(76);

		// accumulator: sum=0; person=Max
		// accumulator: sum=18; person=Peter
		// accumulator: sum=41; person=Pamela
		// accumulator: sum=64; person=David

		System.out.println("\n----------------------------------------\n");

		Integer parallelAgeSum = persons().parallelStream()
				.reduce(0,
						(sum, p) -> {
							System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
							sum += p.age;
							return sum;
						},
						(sum1, sum2) -> {
							System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
							return sum1 + sum2;
						});

		// ----------------------------------------
		//
		// accumulator: sum=0; person=Pamela
		// accumulator: sum=0; person=David
		// accumulator: sum=0; person=Peter
		// combiner: sum1=23; sum2=12
		// accumulator: sum=0; person=Max
		// combiner: sum1=18; sum2=23
		// combiner: sum1=41; sum2=35
		assertThat(parallelAgeSum).isEqualTo(76);
	}


	// ---------------------------------------------------------------------------------

	private List<Person> persons() {
		return Arrays.asList(max, peter, pamela, david);
	}

	private List<Foo> foos() {
		List<Foo> foos = new ArrayList<>();
		foos.add(foo1);
		foos.add(foo2);
		foos.add(foo3);

		foo1.bars.add(bar1Foo1);
		foo1.bars.add(bar2Foo1);
		foo1.bars.add(bar3Foo1);
		foo2.bars.add(bar1Foo2);
		foo2.bars.add(bar2Foo2);
		foo2.bars.add(bar3Foo2);
		foo3.bars.add(bar1Foo3);
		foo3.bars.add(bar2Foo3);
		foo3.bars.add(bar3Foo3);
		
		return foos;
	}
}