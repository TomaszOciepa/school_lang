import { Component, OnInit } from '@angular/core';

export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 2, name: 'Helium', weight: 4.0026, symbol: 'He' },
  { position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li' },
  { position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be' },
  { position: 5, name: 'Boron', weight: 10.811, symbol: 'B' },
  { position: 6, name: 'Carbon', weight: 12.0107, symbol: 'C' },
  { position: 7, name: 'Nitrogen', weight: 14.0067, symbol: 'N' },
  { position: 8, name: 'Oxygen', weight: 15.9994, symbol: 'O' },
  { position: 9, name: 'Fluorine', weight: 18.9984, symbol: 'F' },
  { position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne' },
];

@Component({
  selector: 'app-lessons',
  templateUrl: './lessons.component.html',
  styleUrls: ['./lessons.component.css'],
})
export class LessonsComponent implements OnInit {
  allDays: Array<{ name: string; date: Date; isToday: boolean }> = [];
  weekDays: Array<{ name: string; date: Date; isToday: boolean }> = [];
  monthDays: Array<{ name: string; date: Date; isToday: boolean }> = [];
  startDate: Date = new Date('2024-01-07');
  endDate: Date = new Date('2025-02-15');
  activeDayIndex: number = 0;
  weekStartIndex: number = 0;
  monthName: string = '';
  viewMode: 'weekly' | 'monthly' = 'weekly';
  months: string[] = [
    'Styczeń',
    'Luty',
    'Marzec',
    'Kwiecień',
    'Maj',
    'Czerwiec',
    'Lipiec',
    'Sierpień',
    'Wrzesień',
    'Październik',
    'Listopad',
    'Grudzień',
  ];

  ngOnInit(): void {
    this.normalizeStartDate();
    this.normalizeEndDate();
    this.generateDays();
    this.setActiveDayToToday();
    this.updateWeekDays();
    this.updateMonthName();
  }

  normalizeStartDate(): void {
    const year = this.startDate.getFullYear();
    const month = this.startDate.getMonth();
    this.startDate = new Date(year, month, 1);
  }

  normalizeEndDate(): void {
    const year = this.endDate.getFullYear();
    this.endDate = new Date(year, 11, 31);
  }

  generateDays(): void {
    let currentDate = new Date(this.startDate);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    this.allDays = [];
    while (currentDate <= this.endDate) {
      this.allDays.push({
        name: currentDate.toLocaleDateString('default', { weekday: 'long' }),
        date: new Date(currentDate),
        isToday: currentDate.toDateString() === today.toDateString(),
      });
      currentDate.setDate(currentDate.getDate() + 1);
    }
  }

  setActiveDayToToday(): void {
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const todayIndex = this.allDays.findIndex(
      (day) => day.date.toDateString() === today.toDateString()
    );
    if (todayIndex !== -1) {
      this.activeDayIndex = todayIndex;
    }
  }

  updateWeekDays(): void {
    const currentDay = this.allDays[this.activeDayIndex]?.date || new Date();
    const currentDayIndex = currentDay.getDay();
    const daysToMonday = currentDayIndex === 0 ? -6 : 1 - currentDayIndex;
    this.weekStartIndex = this.activeDayIndex + daysToMonday;

    if (this.weekStartIndex < 0) {
      this.weekStartIndex = 0;
    }

    this.weekDays = this.allDays.slice(
      this.weekStartIndex,
      this.weekStartIndex + 7
    );

    if (this.weekDays.length < 7) {
      this.weekDays = this.allDays.slice(
        this.allDays.length - 7,
        this.allDays.length
      );
      this.weekStartIndex = this.allDays.length - 7;
    }
  }

  updateMonthDays(): void {
    const currentMonth = this.allDays[this.activeDayIndex]?.date.getMonth();
    const currentYear = this.allDays[this.activeDayIndex]?.date.getFullYear();

    if (currentMonth !== undefined && currentYear !== undefined) {
      const firstDayOfMonth = new Date(currentYear, currentMonth, 1);
      const lastDayOfMonth = new Date(currentYear, currentMonth + 1, 0);

      const startDate = new Date(firstDayOfMonth);
      startDate.setDate(
        firstDayOfMonth.getDate() - ((firstDayOfMonth.getDay() + 6) % 7)
      );

      const endDate = new Date(lastDayOfMonth);
      endDate.setDate(
        lastDayOfMonth.getDate() + ((7 - lastDayOfMonth.getDay()) % 7)
      );

      this.monthDays = [];
      let currentDate = new Date(startDate);
      while (currentDate <= endDate) {
        this.monthDays.push({
          name: currentDate.toLocaleDateString('default', { weekday: 'long' }),
          date: new Date(currentDate),
          isToday: currentDate.toDateString() === new Date().toDateString(),
        });
        currentDate.setDate(currentDate.getDate() + 1);
      }

      this.monthName = `${this.months[currentMonth]} ${currentYear}`;
    }
  }

  updateMonthName(): void {
    if (this.viewMode === 'weekly' && this.weekDays.length > 0) {
      const selectedDate = this.weekDays[0]?.date;
      this.monthName = selectedDate.toLocaleDateString('default', {
        month: 'long',
        year: 'numeric',
      });
    }
  }

  previousTab(): void {
    if (this.viewMode === 'weekly' && this.activeDayIndex > 0) {
      this.activeDayIndex--;
      this.updateWeekDays();
      this.updateMonthName();
    } else if (this.viewMode === 'monthly') {
      this.changeMonth(-1);
    }
  }

  nextTab(): void {
    if (
      this.viewMode === 'weekly' &&
      this.activeDayIndex < this.allDays.length - 1
    ) {
      this.activeDayIndex++;
      this.updateWeekDays();
      this.updateMonthName();
    } else if (this.viewMode === 'monthly') {
      this.changeMonth(1);
    }
  }

  changeMonth(direction: number): void {
    const currentMonth = this.allDays[this.activeDayIndex]?.date.getMonth();
    const currentYear = this.allDays[this.activeDayIndex]?.date.getFullYear();

    if (currentMonth !== undefined && currentYear !== undefined) {
      const newMonth = currentMonth + direction;
      this.activeDayIndex = this.allDays.findIndex(
        (day) =>
          day.date.getMonth() === ((newMonth % 12) + 12) % 12 &&
          day.date.getFullYear() === currentYear + Math.floor(newMonth / 12)
      );
      this.updateMonthDays();
    }
  }

  toggleViewMode(): void {
    this.viewMode = this.viewMode === 'weekly' ? 'monthly' : 'weekly';
    if (this.viewMode === 'monthly') {
      this.updateMonthDays();
    } else {
      this.updateWeekDays();
    }
  }

  goToToday(): void {
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const todayIndex = this.allDays.findIndex(
      (day) => day.date.toDateString() === today.toDateString()
    );

    if (todayIndex !== -1) {
      this.activeDayIndex = todayIndex;
      if (this.viewMode === 'weekly') {
        this.updateWeekDays();
      } else if (this.viewMode === 'monthly') {
        this.updateMonthDays();
      }
      this.updateMonthName();
    }
  }

  onTabClick(index: number): void {
    if (this.viewMode === 'weekly') {
      this.activeDayIndex = this.weekStartIndex + index;
      this.updateWeekDays();
      this.updateMonthName();
    }
  }

  displayedColumns: string[] = [
    'demo-position',
    'demo-name',
    'demo-weight',
    'demo-symbol',
  ];
  dataSource = ELEMENT_DATA;
}
