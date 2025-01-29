import { Component, OnInit } from '@angular/core';
import { LessonResponse } from '../core/models/lesson.model';

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
  yearDays: Array<{ name: string; date: Date; isToday: boolean }> = [];
  startDate: Date = new Date('2020-01-07');
  endDate: Date = new Date('2028-12-31');
  activeDayIndex: number = 0;
  weekStartIndex: number = 0;
  monthName: string = '';
  monthIndex: number = new Date().getMonth();
  viewMode: 'weekly' | 'monthly' | 'yearly' = 'weekly';
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

  lessons: LessonResponse[] = [];
  dailyLessons: { [key: string]: LessonResponse[] } = {};
  hours = Array.from({ length: 16 }, (_, i) => i + 7);

  ngOnInit(): void {
    this.loadLessons();
    this.normalizeStartDate();
    this.normalizeEndDate();
    this.generateDays();
    this.setActiveDayToToday();
    this.updateWeekDays();
    this.updateMonthName();
    this.organizeLessonsByDay();
  }

  loadLessons() {
    this.lessons = [
      {
        id: '1',
        eventName: 'Matematyka',
        startDate: '2025-01-29T08:00:00',
        endDate: '2025-01-29T09:00:00',
        teacherId: 1,
        courseId: 'MATH101',
        status: 'scheduled',
        description: 'Lekcja matematyki',
        attendanceList: [],
      },
      {
        id: '2',
        eventName: 'Fizyka',
        startDate: '2025-01-29T10:00:00',
        endDate: '2025-01-29T11:00:00',
        teacherId: 2,
        courseId: 'PHY101',
        status: 'scheduled',
        description: 'Lekcja fizyki',
        attendanceList: [],
      },
    ];
  }

  organizeLessonsByDay() {
    this.lessons.forEach((lesson) => {
      const day = this.formatDate(new Date(lesson.startDate));
      if (!this.dailyLessons[day]) {
        this.dailyLessons[day] = [];
      }
      this.dailyLessons[day].push(lesson);
    });
  }

  getLessons(date: Date, hour: number): LessonResponse[] {
    const formattedDate = this.formatDate(date);
    return (
      this.dailyLessons[formattedDate]?.filter((lesson) => {
        const lessonStart = new Date(lesson.startDate);
        return lessonStart.getHours() === hour;
      }) || []
    );
  }

  formatDate(date: Date): string {
    return date.toISOString().split('T')[0];
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
    } else if (this.viewMode === 'monthly' && this.monthDays.length > 0) {
      const selectedDate = this.monthDays[0]?.date;
      this.monthName = selectedDate.toLocaleDateString('default', {
        month: 'long',
        year: 'numeric',
      });
    } else if (this.viewMode === 'yearly' && this.yearDays.length > 0) {
      const selectedYear = this.yearDays[0]?.date.getFullYear();
      this.monthName = selectedYear.toString();
    }
  }

  previousTab(): void {
    if (this.viewMode === 'weekly' && this.activeDayIndex > 0) {
      this.activeDayIndex--;
      this.updateWeekDays();
      this.updateMonthName();
    } else if (this.viewMode === 'monthly') {
      this.changeMonth(-1);
    } else if (this.viewMode === 'yearly') {
      this.changeYear(-1);
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
    } else if (this.viewMode === 'yearly') {
      this.changeYear(1);
    }
  }

  changeYear(direction: number): void {
    const currentYear = this.allDays[this.activeDayIndex]?.date.getFullYear();

    if (currentYear !== undefined) {
      const newYear = currentYear + direction;
      this.activeDayIndex = this.allDays.findIndex(
        (day) => day.date.getFullYear() === newYear
      );
      this.updateYearDays();
      this.updateMonthName();
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
    if (this.viewMode === 'weekly') {
      this.viewMode = 'monthly';
      this.updateMonthDays();
    } else if (this.viewMode === 'monthly') {
      this.viewMode = 'yearly';
      this.updateYearDays();
    } else {
      this.viewMode = 'weekly';
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
      } else if (this.viewMode === 'yearly') {
        this.updateYearDays();
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
    console.log('indeks: ', this.activeDayIndex);
  }

  onDayClick(day: any) {
    this.setWeekDaysForDate(day.date);

    const newIndex = this.weekDays.findIndex(
      (d) => d.date.toDateString() === new Date(day.date).toDateString()
    );

    if (newIndex !== -1) {
      this.activeDayIndex = this.weekStartIndex + newIndex;
    } else {
      console.warn('Błąd: nie znaleziono dnia w weekDays');
    }

    this.viewMode = 'weekly';
  }

  setWeekDaysForDate(selectedDate: Date) {
    const date = new Date(selectedDate);
    const dayOfWeek = date.getDay();

    const startOfWeek = new Date(date);
    startOfWeek.setDate(date.getDate() - (dayOfWeek === 0 ? 6 : dayOfWeek - 1));

    this.weekDays = Array.from({ length: 7 }, (_, i) => {
      const day = new Date(startOfWeek);
      day.setDate(startOfWeek.getDate() + i);
      return {
        date: day,
        name: this.getDayName(day),
        isToday: day.toDateString() === new Date().toDateString(),
      };
    });
  }

  getDayName(date: Date): string {
    const dniTygodnia = ['Nd', 'Pon', 'Wt', 'Śr', 'Czw', 'Pt', 'Sob'];
    return dniTygodnia[date.getDay()];
  }
  updateYearDays(): void {
    const currentYear = this.allDays[this.activeDayIndex]?.date.getFullYear();
    this.yearDays =
      currentYear !== undefined
        ? this.allDays.filter((day) => day.date.getFullYear() === currentYear)
        : [];
  }

  getDaysForMonth(
    monthIndex: number
  ): { name: string; date: Date; isToday: boolean }[] {
    return (this.yearDays || []).filter(
      (d) => d.date.getMonth() === monthIndex
    );
  }

  getEmptyDaysBeforeMonth(monthIndex: number): number[] {
    const year = this.allDays[this.activeDayIndex]?.date.getFullYear();
    if (year === undefined) return [];

    const firstDayOfMonth = new Date(year, monthIndex, 1);
    const weekday = firstDayOfMonth.getDay();

    const adjustedWeekday = weekday === 0 ? 6 : weekday - 1;

    return new Array(adjustedWeekday).fill(0);
  }

  isWeekend(date: Date): boolean {
    const dayOfWeek = date.getDay();
    return dayOfWeek === 0 || dayOfWeek === 6;
  }

  hasLessons(date: Date): boolean {
    const formattedDate = this.formatDate(date); // Formatujemy datę na "YYYY-MM-DD"
    return this.dailyLessons[formattedDate]?.length > 0; // Sprawdzamy, czy są lekcje
  }

  displayedColumns: string[] = [
    'demo-position',
    'demo-name',
    'demo-weight',
    'demo-symbol',
  ];
  dataSource = ELEMENT_DATA;
}
