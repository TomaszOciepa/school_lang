import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import { Course } from 'src/app/modules/core/models/course.model';

import {
  Lesson,
  LessonResponse,
} from 'src/app/modules/core/models/lesson.model';
import { User } from 'src/app/modules/core/models/user.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css'],
})
export class CalendarComponent implements OnChanges, OnInit {
  @Input() lessons!: Lesson[];

  constructor(
    private teacherService: TeacherService,
    private courseService: CourseService
  ) {}

  ngOnInit(): void {
    setTimeout(() => {
      this.isLoading = false;
    }, 1000);

    this.getTeachers();
    this.getCourses();
  }

  isLoading = true;

  allDays: Array<{ name: string; date: Date; isToday: boolean }> = [];
  weekDays: Array<{ name: string; date: Date; isToday: boolean }> = [];
  monthDays: {
    name: string;
    date: Date;
    isToday: boolean;
    isCurrentMonth: boolean;
  }[] = [];

  teachersList: User[] = [];
  courseList: Course[] = [];
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

  dailyLessons: { [key: string]: LessonResponse[] } = {};

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['lessons'] && changes['lessons'].currentValue) {
      this.normalizeStartDate();
      this.normalizeEndDate();
      this.generateDays();
      this.setActiveDayToToday();
      this.updateWeekDays();
      this.updateMonthName();
      this.organizeLessonsByDay();
    }
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

  getLessons(dayDate: Date) {
    return this.lessons
      .filter((lesson) => {
        const lessonDate = new Date(lesson.startDate);
        return (
          lessonDate.getFullYear() === dayDate.getFullYear() &&
          lessonDate.getMonth() === dayDate.getMonth() &&
          lessonDate.getDate() === dayDate.getDate()
        );
      })
      .sort(
        (a, b) =>
          new Date(a.startDate).getTime() - new Date(b.startDate).getTime()
      );
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
        const isCurrentMonth = currentDate.getMonth() === currentMonth;
        this.monthDays.push({
          name: currentDate.toLocaleDateString('default', { weekday: 'long' }),
          date: new Date(currentDate),
          isToday: currentDate.toDateString() === new Date().toDateString(),
          isCurrentMonth,
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
      const selectedDate = this.monthDays[this.activeDayIndex]?.date;
      this.monthName = selectedDate.toLocaleDateString('default', {
        month: 'long',
        year: 'numeric',
      });
    } else if (this.viewMode === 'yearly' && this.yearDays.length > 0) {
      const selectedYear = this.yearDays[0]?.date.getFullYear();
      this.monthName = 'Rok ' + selectedYear.toString();
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
    const formattedDate = this.formatDate(date);
    return this.dailyLessons[formattedDate]?.length > 0;
  }

  formatDate(date: Date): string {
    return date.toISOString().split('T')[0];
  }

  getLessonStatus(lesson: any): string {
    switch (lesson.status) {
      case 'INACTIVE':
        return 'Oczekiwana';
      case 'ACTIVE':
        return 'W trakcie';
      case 'FINISHED':
        return 'Zakończona';
      default:
        return 'Nieznany';
    }
  }
  getStatusColor(lesson: any): string {
    switch (lesson.status) {
      case 'INACTIVE':
        return '#FFD700';
      case 'ACTIVE':
        return '#4CAF50';
      case 'FINISHED':
        return '#9E9E9E';
      default:
        return 'transparent';
    }
  }

  getTeachers(): void {
    this.teacherService.getTeachers('ACTIVE').subscribe({
      next: (response) => {
        this.teachersList = response;
      },
      error: (err) => {
        console.log(err);
      },
      complete: () => {},
    });
  }

  // Funkcja do wyszukiwania nauczyciela po id
  getTeacherById(teacherId: number): User | undefined {
    return this.teachersList.find((teacher) => teacher.id === teacherId);
  }

  getLanguageName(language: string): string {
    switch (language) {
      case 'ENGLISH':
        return 'Angielski';
      case 'POLISH':
        return 'Polski';
      case 'GERMAN':
        return 'Niemiecki';
      default:
        return 'Nieznany';
    }
  }

  getCourses(): void {
    console.log('pobieram kursy');
    this.courseService.getAllByStatus().subscribe({
      next: (response) => {
        this.courseList = response;
      },
      error: (err) => {
        console.log(err);
      },
      complete: () => {},
    });
  }

  getCourseById(courseId: string): Course | undefined {
    return this.courseList.find((course) => course.id === courseId);
  }
}
