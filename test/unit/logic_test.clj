(ns unit.logic-test
  (:require [clojure.test :refer :all]
            [organize-expenses.logic :refer :all]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.clojure-test :refer (defspec)]
            [clojure.test.check.properties :as prop]
            [schema-generators.generators :as g]
            [schema-generators.complete :as c]
            [matcher-combinators.test :refer :all]
            [schema.core :as s])
  (:use clojure.pprint))

(s/def PosNum (s/constrained s/Num #(>= % 0) 'bigger-than-zero))

(def financial-record
  {:id s/Uuid
   :value PosNum
   :description s/Str
   :type (s/enum :income :expense)
   :category (s/enum :other :food :transport :tech :house)
   })

;; Testing using generators
(defspec test-duplicated-description-same-month-gen 20
         (prop/for-all
           [sample-description (gen/not-empty gen/string-alphanumeric)]
           (testing "Returns a value when the description is already on the database for this month"
             (is (not= nil
                       (duplicated-description-same-month? sample-description [{:description sample-description} {:description "AnotherThing"}]))))
           (testing "Returns nil (false) when there is no match of description given the description and array of descriptions "
             (is (not (duplicated-description-same-month? sample-description [{:description "Unlikely to have the same description"}]))))
           (testing "Returs nil (false) when there is no description on the list"
             (is (not (duplicated-description-same-month? sample-description []))))))

;; Testing border (edge) cases
(deftest test-summary-report
  (let [income1 (c/complete {:value 4000.0 :type :income} financial-record)
        income2 (c/complete {:value 3000.0 :type :income} financial-record)
        expense1 (c/complete {:value 800.0 :type :expense :category :food} financial-record)
        expense2 (c/complete {:value 500.0 :type :expense :category :transport} financial-record)]
    (testing "total income and total expense are correctly displayed if the list is not null"
      (is (match? {:total-income 7000.0, :total-expenses 1300.0}
                  (summary-report [income1 income2] [expense1 expense2]))))
    (testing "if expenses is empty and there are incomes, the final-balance is positive"
      (is (> (:final-balance (summary-report [income1 income2] [])) 0)))
    (testing "if incomes is empty and there are expenses, the final-balance is negative"
      (is (< (:final-balance (summary-report [] [expense1 expense2])) 0)))
    (testing "if the sum of incomes are greater than the sum of expenses, the final balance is positive"
      (is (> (:final-balance (summary-report [income1 income2] [expense1 expense2])) 0)))
    (testing "If the lists are empty, the return is 0 for number fields and empty list for grouped category"
      (is (= {:total-income 0, :total-expenses 0, :final-balance 0, :grouped-category []}
             (summary-report [] []))))))

