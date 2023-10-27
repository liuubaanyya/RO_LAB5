package main

import (
	"fmt"
	"math/rand"
	"sync"
)

func modifyArrays(array []int, limit int, wg *sync.WaitGroup) {
	index := rand.Intn(len(array))

	randomNumber := rand.Intn(2)
	if randomNumber == 0 {
		if array[index] < limit {
			array[index] += 1
		}
	} else {
		if array[index] > limit*-1 {
			array[index] -= 1
		}
	}
	wg.Done()
}

func createArrays(sizeOfArrays, limit int) []int {
	var array []int
	for i := 0; i < sizeOfArrays; i += 1 {
		array = append(array, rand.Intn(limit))
	}
	return array
}

func printArrays(arrays [][]int) {
	for i := 0; i < len(arrays); i++ {
		fmt.Println(arrays[i])
	}
	fmt.Println()
}

func sumsAreEqual(arrays [][]int) bool {
	sums := make([]int, len(arrays))

	for i := 0; i < len(arrays); i++ {
		for j := 0; j < len(arrays[i]); j++ {
			sums[i] += arrays[i][j]
		}
	}

	if sums[0] == sums[1] && sums[0] == sums[2] {
		fmt.Println("Sum of the elements of each array =  ", sums[0])
		return true
	} else {
		return false
	}
}
func main() {

	wg := new(sync.WaitGroup)
	var NumberOfArrays = 3
	var SizeOfArrays = 10
	var Limit = 5

	var arrays [][]int
	for i := 0; i < NumberOfArrays; i += 1 {
		arrays = append(arrays, createArrays(SizeOfArrays, Limit))
	}

	for !sumsAreEqual(arrays) {
		wg.Add(NumberOfArrays)
		for i := 0; i < NumberOfArrays; i += 1 {
			go modifyArrays(arrays[i], Limit, wg)
			printArrays(arrays)
		}
		wg.Wait()
	}

	printArrays(arrays)
}
