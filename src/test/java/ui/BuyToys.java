package ui;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import environment.TestBase;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

class BuyToys extends TestBase {


    @Test
    void testToTest() {
        goToHomePage();
        addToysToCard(4);
        goToCartWrapper();
        removeCartItem();
        clickToLogo();
    }

    void goToHomePage() {
        open("http://litecart.stqa.ru/en/");
    }

    void goToCartWrapper() {
        $x("//div[@id='cart-wrapper']").click();
    }

    void clickToLogo() {
        $x("//div[@id='logotype-wrapper']").click();
    }

    void addToysToCard(int count) {
        for (int i = 1; i <= count; i++) {
            //           $x("//div[@id='box-most-popular']//ul[@class='listing-wrapper products']").waitUntil(visible, 3000);
            $x("//div[@id='box-most-popular']//ul//li[1]").click();
            if ($x("//select[@name='options[Size]']").exists()) {
                chooseSize("Small");
            }
            $x("//button[@name='add_cart_product']").click();
            $x("//span[@class='quantity']//..//span[1]").waitUntil(text(String.valueOf(i)), 2000);
            clickToLogo();
        }
    }

    void chooseSize(String size) {
        $x("//select[@name='options[Size]']").selectOptionByValue(size);
    }

    void removeCartItem() {
        int itemsCount = $$x("//table[(@class='dataTable rounded-corners')]//td[@class='item']").size();

        for (int i = itemsCount; i >= 1; i--) {
            SelenideElement subtotalPrice = $x("//strong[contains(text(),'Subtotal')]//..//..//td[2]");
            SelenideElement totalPrice = $x(" //tr[@class='footer']//td[2]");
            double subTotal = Double.parseDouble(subtotalPrice.getText().replaceAll("\\$", ""));
            double totalPayment = Double.parseDouble(totalPrice.getText().replaceAll("\\$", ""));
            int valueOfToys = $$x("//table[(@class='dataTable rounded-corners')]//td[@class='item']").size();

            if ($x("//ul[(@class='shortcuts')]").exists()) {
                $x("//ul[(@class='shortcuts')]//li[1]").click();
            }

            if (valueOfToys > 1) {
                $x("//button[@name='remove_cart_item']").click();
                $$x("//table[(@class='dataTable rounded-corners')]//td[@class='item']").shouldHave(CollectionCondition.size(i - 1));
                double updatedTotalPayment = Double.parseDouble(totalPrice.getText().replaceAll("\\$", ""));
                $$x("//td[@class='unit-cost']").shouldHave(CollectionCondition.size(i - 1));
                Assert.assertTrue(totalPayment >= updatedTotalPayment);
                Assert.assertTrue(subTotal <= totalPayment);
            } else {
                $x("//button[@name='remove_cart_item']").click();
                $x("//em[contains(text(),'There are no items in your cart.')]").shouldBe(visible);
                $x("//a[contains(text(),'<< Back')]").click();

            }

        }
    }
}


