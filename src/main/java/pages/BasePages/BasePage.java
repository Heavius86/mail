package pages.BasePages;


import static infrastructure.drivers.DriverActions.*;


public abstract class BasePage {

    /**
     * Открыть страницу
     *
     * @param fullUrl путь до ресурса
     */
    public void open(String fullUrl) {
        openPage(fullUrl);
        checkAlert();
    }

    /**
     * Обновить страницу
     */
    public void refreshPage() {
        refresh();
        checkAlert();
    }

    /**
     * Нажать кнопку назад (действие браузера)
     */
    public void clickBack() {
        back();
        checkAlert();
    }


}