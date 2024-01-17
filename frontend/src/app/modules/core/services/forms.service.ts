import { Injectable } from '@angular/core';
import { FormControl } from '@angular/forms';

@Injectable({
  providedIn: 'root',
})
export class FormsService {
  constructor() {}

  getErrorMessage(control: FormControl) {
    if (control.hasError('required')) {
      return 'Musisz wpisać jakąś wartość!';
    }

    if (control.hasError('minlength')) {
      return 'Przekazałeś za mało znaków w kontrolce';
    }

    if (control.hasError('maxlength')) {
      return 'Przekazałeś za duzo znaków w kontrolce';
    }

    if (control.hasError('invalidPostcode')) {
      return 'Kod pocztowy powinien być w formacie xx-xxx ';
    }

    return control.hasError('email') ? 'Nieprwidłowy adres email' : '';
  }
}
