import { Injectable } from '@angular/core';
import { FormControl } from '@angular/forms';

@Injectable({
  providedIn: 'root',
})
export class FormsService {
  constructor() {}

  getErrorMessage(control: FormControl) {
    if (control.hasError('required')) {
      return 'Pole jest wymagane!';
    }

    if (control.hasError('minlength')) {
      return 'Przekazałeś za mało znaków w kontrolce';
    }

    if (control.hasError('maxlength')) {
      return 'Przekazałeś za duzo znaków w kontrolce';
    }
    if (control.hasError('maxlength')) {
      return 'Przekazałeś za duzo znaków w kontrolce';
    }
    if (control.hasError('max')) {
      return 'Przekazałeś za duzo znaków w kontrolce';
    }
    if (control.hasError('min')) {
      return 'Przekazałeś za mało znaków w kontrolce';
    }
    if (control.hasError('invalidPostcode')) {
      return 'Kod pocztowy powinien być w formacie xx-xxx ';
    }

    if (control.hasError('pattern')) {
      return 'Nieprawidłowe dane';
    }
    return control.hasError('email') ? 'Nieprwidłowy adres email' : '';
  }
}
